class Lugar {
  String correoElectronico;
  String descripcion;
  String direccion;
  String horaApertura;
  String horaCierre;
  double latitud;
  double longitud;
  String nombre;
  double promedio;
  int telefono;
  String tipo;
  int precioMaximo;
  int precioMinimo;
  List<String> resenias;
  String key;

  Lugar();

  Lugar.construct(this.nombre, this.descripcion);
}