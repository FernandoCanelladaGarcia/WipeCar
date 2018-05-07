package tfg.android.fcg.modelo;

public class Login {

    //Variables Globales

    private String idUser;
    private String nombre;
    private String email;
//    private String telefono;
//    private boolean rol;

    //Constructor

    public Login(String idUser, String nombre, String email){
        this.idUser = idUser;
        this.nombre = nombre;
        this.email = email;
//        this.telefono = telefono;
//        this.rol = rol;
    }
    public Login(){

    }

    //Getters & Setters

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public String getTelefono() {
//        return telefono;
//    }
//
//    public void setTelefono(String telefono) {
//        this.telefono = telefono;
//    }
//
//    public boolean isRol() {
//        return rol;
//    }
//
//    public void setRol(boolean rol) {
//        this.rol = rol;
//    }

}
