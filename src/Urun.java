public class Urun {
    private int no;
    private String description;
    private double price;
    private int quantity;
    public Urun(int no, String description, double price, int quantity){
        setNo(no);
        setDescription(description);
        setPrice(price);
        setQuantity(quantity);
    }
    public int getNo() {
        return no;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
