# Overview

The current emergency services system is dated facilitating only a single form of communication - voice. This form of communication is limiting, slow and often leads to communication errors. SOS is a platform that addresses these issues through a simplistic app and user-friendly website. We aim to increase the flow of communication between citizens and 911 operators by creating additional pipelines of communication: multimedia, text, caller info, translation services, and artificial intelligence.

## SOS Website

This is the main website. It is hosted on a Windows Web Server, where it is updated and maintained via python scripts.

## Windows Web Server

This Windows Server 2012 r2 server hosts the SOS website and is responsible for updating it. It also hosts the FTP server for the Call Server and the Android App.

## Android App

This is the android application, developed in Android Studio.

## Call Server

This is the Ubuntu server responsible for managing TTS services and phone calls.

# Libraries

A variety of libraries were used in this project.

- OKHTTP
- IBM Watson
- Apache Commons
- Google Play Services
- Google Voice
- Google Location APIs
- Location IQ API
- Playsound
- Pavucontrol
