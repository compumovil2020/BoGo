import 'dart:math';

import 'package:bogo_admin/Utils/colors.dart';
import 'package:bogo_admin/placeDescription.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'Lugar.dart';

class ReviewActivity extends StatefulWidget
{
  ReviewActivity();
  @override
  ReviewState createState() => ReviewState();
}
var places = [];
class ReviewState extends State<ReviewActivity>
{
  final myRef = FirebaseDatabase.instance.reference().child("lugaresPendientes");
  bool _loaded, _empty;
  @override
  void initState() {
    _loaded = false;
    _empty = false;
    myRef.onValue.listen((event)
    {
      DataSnapshot dataSnapshot = event.snapshot;
      setState(() {
        _loaded = true;
        _empty = false;
        print(dataSnapshot.value);
        if(dataSnapshot.value != null) {
          Map<dynamic, dynamic> map = dataSnapshot.value;
          places = [];
          for (var entry in map.entries) {
            var place = entry.value;
            Lugar l = Lugar();
            l.descripcion = place["descripcion"];
            l.nombre = place["nombre"];
            l.correoElectronico = place["correoElectronico"];
            l.direccion = place["direccion"];
            l.horaApertura = place["horaApertura"];
            l.horaCierre = place["horaCierre"];
            l.latitud = place["latitud"];
            l.longitud = place["longitud"];
            l.precioMaximo = place["precioMaximo"];
            l.precioMinimo = place["precioMinimo"];
            l.promedio = place["promedio"].toDouble();
            l.telefono = place["telefono"];
            l.tipo = place["tipo"];
            l.key = entry.key;
            places.add(l);
          }
        }
        else {
          setState(() {
            _empty = true;
          });
        }
      });
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text("Guess Game"),
          backgroundColor: CustomColors.createMaterialColor(CustomColors.BogoRed),
        ),
        body:
        (_loaded == true ?
          ( _empty != true ? BodyLayout()
              : Center( child: Text("No hay lugares pendientes", style: Theme.of(context).textTheme.headline5.merge( TextStyle( color: Colors.black, fontWeight: FontWeight.bold))))
          ) : Text("Loading...", style: Theme.of(context).textTheme.headline4,)),
            );
  }
}

class BodyLayout extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return _buildItem(context);
  }
}

// replace this function with the code in the examples
Widget _buildItem(BuildContext context) {
  return ListView.builder(
    itemCount: places.length,
    itemBuilder: (context, index) {
      return ListTile(
        title: Text(places[index].nombre, overflow: TextOverflow.fade,),
        subtitle: Text(places[index].descripcion, overflow: TextOverflow.fade, maxLines: 2,),
        isThreeLine: true,
        onTap: (){ goToPlaceDescription(context, index); },
      );
    },
  );
}

void goToPlaceDescription(BuildContext context, int index)
{
  Navigator.push( context,
    MaterialPageRoute<void>(builder: (_) => PlaceDescriptionActivity( places[index] )),
  );
}