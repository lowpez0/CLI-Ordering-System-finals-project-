public class Product {

    final private String PRODUCT_NAME;
    final private String SPECIFICATIONS;
    final private String REVIEW;
    final private int PRICE;

    public Product(String PRODUCT_NAME, String SPECIFICATIONS, String REVIEW, int PRICE) {
        this.PRODUCT_NAME = PRODUCT_NAME;
        this.SPECIFICATIONS = SPECIFICATIONS;
        this.REVIEW = REVIEW;
        this.PRICE = PRICE;
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
