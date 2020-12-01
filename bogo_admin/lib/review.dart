import 'dart:math';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ReviewActivity extends StatefulWidget
{
  ReviewActivity();
  @override
  _GuessGameState createState() => _GuessGameState();
}

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
          backgroundColor: Colors.deepPurple,
        ),
        body: Padding(
          padding: EdgeInsets.all(20),
          child: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
            ),
          ),
        )
    );
  }
}