package midterm;

public class BitCoin extends Coin {
    private double amount;

    public BitCoin(double amount) {
        super("bitcoin");
        this.amount = amount;
    }

    /**
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public double getAsUSD() {
        return this.getAmount() * 47806.80;
    }
}
