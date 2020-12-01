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
var pepe = [];
class ReviewState extends State<ReviewActivity>
{
  final myRef = FirebaseDatabase.instance.reference().child("lugares");
  var _loaded;
  @override
  void initState() {
    _loaded = false;
    myRef.once().then((DataSnapshot dataSnapshot)
    {
      setState(() {
        _loaded = true;
        Map<dynamic, dynamic> map = dataSnapshot.value;
        for(var entry in map.entries)
        {
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
          pepe.add(l);
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
          (_loaded==true ? BodyLayout() : Text("Loading...")),
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
    itemCount: pepe.length,
    itemBuilder: (context, index) {
      return ListTile(
        title: Text(pepe[index].nombre, overflow: TextOverflow.fade,),
        subtitle: Text(pepe[index].descripcion, overflow: TextOverflow.fade, maxLines: 2,),
        isThreeLine: true,
        onTap: (){ goToPlaceDescription(context, index); },
      );
    },
  );
}

void goToPlaceDescription(BuildContext context, int index)
{
  Navigator.push( context,
    MaterialPageRoute<void>(builder: (_) => PlaceDescriptionActivity( pepe[index] )),
  );
}