public class Product {

    private final String PRODUCT_NAME;
    private final String SPECIFICATIONS;
    private final String REVIEW;
    private final int PRICE;
    private final String CATEGORY;

    public Product(String PRODUCT_NAME, String SPECIFICATIONS, String REVIEW, int PRICE, String CATEGORY) {
        this.PRODUCT_NAME = PRODUCT_NAME;
        this.SPECIFICATIONS = SPECIFICATIONS;
        this.REVIEW = REVIEW;
        this.PRICE = PRICE;
        this.CATEGORY = CATEGORY;
    }

    public String getPRODUCT_NAME() {
        return this.PRODUCT_NAME;
    }

    public String getSPECIFICATIONS() {
        return this.SPECIFICATIONS;
    }

    public String getREVIEW() {
        return this.REVIEW;
    }

    public int getPRICE() {
        return this.PRICE;
    }

    @Override
    public String toString() {
        return "Product{" +
                "PRODUCT_NAME='" + PRODUCT_NAME + '\'' +
                ", SPECIFICATIONS='" + SPECIFICATIONS + '\'' +
                ", REVIEW='" + REVIEW + '\'' +
                ", PRICE=" + PRICE +
                '}';
    }
}
