# Call Server

The call server was necessary to utilize Google Voice to make prerecorded phone calls. `play_audio.py` inputs the text collected from the FTP server to the Watson tts API, and plays it. A Google Voice phone call is running in parallel, and the input is set to the monitor. This plays the generated .wav through a live phone call.
