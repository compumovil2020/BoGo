
import 'package:bogo_admin/Utils/colors.dart';
import 'package:bogo_admin/review.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:firebase_storage/firebase_storage.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'Lugar.dart';

Lugar lugarDes;

class PlaceDescriptionActivity extends StatefulWidget
{

  PlaceDescriptionActivity(Lugar lugar)
  {
    lugarDes = lugar;
  }

  @override
  PlaceDescription createState() => PlaceDescription();
}

class PlaceDescription extends State<PlaceDescriptionActivity>
{
  final myRef = FirebaseDatabase.instance.reference().child("lugaresPendientes").child(lugarDes.key);
  final myRefLugares = FirebaseDatabase.instance.reference().child("lugares");
  var storage = FirebaseStorage.instance;
  final _scaffoldKey = GlobalKey<ScaffoldState>();
  @override
  void initState() {
    super.initState();
    getImage();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      key: _scaffoldKey,
      appBar: AppBar(
        title: Text(lugarDes.nombre),
        backgroundColor: CustomColors.createMaterialColor(CustomColors.BogoRed),
      ),
      body: Padding(
        padding: EdgeInsets.all(20),
        child: Column(
          children: [
            FutureBuilder(
              future: getImage(),
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.done)
                {
                  print(snapshot);
                  return Image.network(snapshot.data);
                }
                return CircularProgressIndicator();
              },
            ),
            Text(lugarDes.descripcion, style: Theme.of(context).textTheme.bodyText1,)
          ],
        ),
      ),
      persistentFooterButtons: [
        Container(
          width: 80,
          height: 80,
          child: new RawMaterialButton(
              onPressed: approvePlace,
            fillColor: Colors.green
            ,
            shape: new CircleBorder(),
            elevation: 0.0,
            child: Icon(Icons.check, color: Colors.white,),

          ),
        ),
        Container(
          width: 80,
          height: 80,
          child: new RawMaterialButton(
            onPressed: denyPlace,
            fillColor: CustomColors.createMaterialColor(CustomColors.BogoRed),
            shape: new CircleBorder(),
            elevation: 0.0,
            child: Icon(Icons.block, color: Colors.white,),

          ),
        ),
      ],
    );
  }


  void approvePlace()
  {
    myRefLugares.child(lugarDes.key).set(<String, Object>{
      "correoElectronico": lugarDes.correoElectronico
    ,"descripcion": lugarDes.descripcion
    ,"direccion": lugarDes.direccion
    ,"horaApertura": lugarDes.horaApertura
    ,"horaCierre": lugarDes.horaCierre
    ,"latitud": lugarDes.latitud
    ,"longitud": lugarDes.longitud
    ,"nombre": lugarDes.nombre
    ,"promedio": lugarDes.promedio
    ,"telefono": lugarDes.telefono
    ,"tipo": lugarDes.tipo
    ,"precioMaximo": lugarDes.precioMaximo
    ,"precioMinimo": lugarDes.precioMinimo
    }).then((value) => {
      myRef.remove().then((value)
      {
        _scaffoldKey.currentState.showSnackBar(SnackBar(
          content: Text("Este lugar ha sido aceptado!"),
        ));
        Navigator.push( context,
        MaterialPageRoute<void>(builder: (_) => ReviewActivity( )),
        );
      })
    });

  }

  void denyPlace()
  {
     storage.ref().child("lugares").child(lugarDes.key).child("place.jpg").delete().then((value)
      {
        myRef.remove().then((value) {
        _scaffoldKey.currentState.showSnackBar(SnackBar(
          content: Text("Este lugar ha sido rechazado!"),
        ));
        Navigator.push( context,
          MaterialPageRoute<void>(builder: (_) => ReviewActivity( )),
        );
      });
    });


  }

  Future<dynamic> getImage() async {
    StorageReference imageLink = storage.ref().child("lugares").child(lugarDes.key).child("place.jpg");
    final imageURL = await imageLink.getDownloadURL();
    return imageURL;
  }
}