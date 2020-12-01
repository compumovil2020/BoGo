
import 'package:bogo_admin/Utils/colors.dart';
import 'package:firebase_database/firebase_database.dart';
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

  final myRef = FirebaseDatabase.instance.reference().child("lugares");
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(lugarDes.nombre),
        backgroundColor: CustomColors.createMaterialColor(CustomColors.BogoRed),
      ),
      body: Padding(
        padding: EdgeInsets.all(20),
        child: Column(
          children: [
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
    
  }

  void denyPlace()
  {

  }
}