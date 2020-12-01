import 'dart:math';

import 'package:bogo_admin/Utils/colors.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ReviewActivity extends StatefulWidget
{
  ReviewActivity();
  @override
  _GuessGameState createState() => _GuessGameState();
}
var pepe = ["Pepe 1", "Pepe 2"];
class _GuessGameState extends State<ReviewActivity>
{

  @override
  void initState() {
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
                BodyLayout(),
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
      print(pepe[index]);
      return ListTile(
        title: Text(pepe[index]),
      );
    },
  );
}