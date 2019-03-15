package www.mensajerosurbanos.com.co.firebasemvp.Modelo;

public class ProductoModel {

    private String nombreProducto;
    private float precioProducto;
    private String image;



    public ProductoModel(){}

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public float getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(float precioProducto) {
        this.precioProducto = precioProducto;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String imagen) {
        this.image = image;
    }
}
