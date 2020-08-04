package im.mak.waves.transactions;

import im.mak.waves.transactions.account.PublicKey;
import im.mak.waves.transactions.common.Amount;
import im.mak.waves.transactions.common.Proof;
import im.mak.waves.transactions.common.WavesJConfig;
import im.mak.waves.transactions.exchange.AssetPair;
import im.mak.waves.transactions.exchange.Order;
import im.mak.waves.transactions.exchange.OrderType;

import java.io.IOException;
import java.util.*;

public class ExchangeTransaction extends Transaction {

    public static final int TYPE = 7;
    public static final int LATEST_VERSION = 3;
    public static final long MIN_FEE = 300_000;

    private final List<Order> orders;
    private final long amount;
    private final long price;
    private final long buyMatcherFee;
    private final long sellMatcherFee;

    public ExchangeTransaction(PublicKey sender, Order order1, Order order2, long amount, long price) {
        this(sender, order1, order2, amount, price, MIN_FEE, MIN_FEE, WavesJConfig.chainId(), //todo calc proportionally instead of MIN_FEE
                Amount.of(MIN_FEE), System.currentTimeMillis(), LATEST_VERSION, Proof.emptyList());
    }

    public ExchangeTransaction(PublicKey sender, Order order1, Order order2, long amount, long price,
                               long buyMatcherFee, long sellMatcherFee, byte chainId, Amount fee, long timestamp,
                               int version, List<Proof> proofs) {
        super(TYPE, version, chainId, sender, fee, timestamp, proofs);
        if (order1 == null) throw new IllegalArgumentException("Buy order can't be null");
        if (order2 == null) throw new IllegalArgumentException("Sell order can't be null");
        if (order1.type() == order2.type()) throw new IllegalArgumentException("Order types must be different");
        if (!order1.matcher().equals(order2.matcher()))
            throw new IllegalArgumentException("Matcher's public key in orders must be equal");
        if (!sender.equals(order1.matcher()))
            throw new IllegalArgumentException("Order matcher must be equal to the transaction sender");
        if (!order1.assetPair().equals(order2.assetPair()))
            throw new IllegalArgumentException("Asset pair in orders must be equal");

        this.orders = Collections.unmodifiableList(Arrays.asList(order1, order2));
        this.amount = amount;
        this.price = price;
        this.buyMatcherFee = buyMatcherFee;
        this.sellMatcherFee = sellMatcherFee;
    }

    public static ExchangeTransaction fromBytes(byte[] bytes) throws IOException {
        return (ExchangeTransaction) Transaction.fromBytes(bytes);
    }

    public static ExchangeTransaction fromJson(String json) throws IOException {
        return (ExchangeTransaction) Transaction.fromJson(json);
    }

    public static ExchangeTransactionBuilder with(Order buy, Order sell) {
        return new ExchangeTransactionBuilder(buy, sell);
    }

    public AssetPair assetPair() {
        return this.orders.get(0).assetPair();
    }

    public List<Order> orders() {
        return orders;
    }

    public Order buyOrder() {
        return orders.stream()
                .filter(o -> o.type() == OrderType.BUY)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "ExchangeTransaction " + id().toString() + "doesn't have buy order"));
    }

    public Order sellOrder() {
        return orders.stream()
                .filter(o -> o.type() == OrderType.SELL)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "ExchangeTransaction " + id().toString() + "doesn't have sell order"));
    }

    public long amount() {
        return amount;
    }

    public long price() {
        return price;
    }

    public long buyMatcherFee() {
        return buyMatcherFee;
    }

    public long sellMatcherFee() {
        return sellMatcherFee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExchangeTransaction that = (ExchangeTransaction) o;
        return amount == that.amount &&
                price == that.price &&
                buyMatcherFee == that.buyMatcherFee &&
                sellMatcherFee == that.sellMatcherFee &&
                Objects.equals(orders, that.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orders, amount, price, buyMatcherFee, sellMatcherFee);
    }

    public static class ExchangeTransactionBuilder
            extends TransactionBuilder<ExchangeTransactionBuilder, ExchangeTransaction> {
        private final Order order1;
        private final Order order2;
        private long amount;
        private long price;
        private long buyMatcherFee;
        private long sellMatcherFee;

        protected ExchangeTransactionBuilder(Order order1, Order order2) {
            super(LATEST_VERSION, MIN_FEE);
            this.order1 = order1;
            this.order2 = order2;
            this.amount = Math.min(this.order1.amount().value(), this.order2.amount().value());
//todo            this.price = normalized?
            this.buyMatcherFee = this.order1.fee().value(); //todo proportionally
            this.sellMatcherFee = this.order2.fee().value();
        }
        
        public ExchangeTransactionBuilder amount(long amount) {
            this.amount = amount;
            return this;
        }
        
        public ExchangeTransactionBuilder price(long price) {
            this.price = price;
            return this;
        }
        
        public ExchangeTransactionBuilder buyMatcherFee(long buyMatcherFee) {
            this.buyMatcherFee = buyMatcherFee;
            return this;
        }
        
        public ExchangeTransactionBuilder sellMatcherFee(long sellMatcherFee) {
            this.sellMatcherFee = sellMatcherFee;
            return this;
        }

        protected ExchangeTransaction _build() {
            return new ExchangeTransaction(sender, order1, order2, amount, price, buyMatcherFee, sellMatcherFee,
                    chainId, feeWithExtra(), timestamp, version, Proof.emptyList());
        }
    }

}
