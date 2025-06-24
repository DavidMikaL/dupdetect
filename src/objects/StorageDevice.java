package objects;

public class StorageDevice {
    private int id;
    private String name;

    private String price;
    private String brand;
    private String description;
    private String category;

    @Override
    public String toString() {
        return "StorageDevice{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", brand='" + brand + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category +
                '}';
    }
}
