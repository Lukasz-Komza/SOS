import ftplib
import os
from playsound import playsound

ID = input()

def grabFile(location,name):
    localfile = open(name, 'wb')
    server.retrbinary('RETR '+location+name, localfile.write, 1024)
    server.quit()
    localfile.close()
server = ftplib.FTP()
server.connect('ftp.komza.com', 21)
server.login('ftp.komza.com|lkomza','not_the_real_password ;)')

print("/TTS/"+ID+".txt")
server.dir()
grabFile("/TTS/",ID+".txt")

file = open(ID+".txt")
text = file.read()
text+=" My information is available at S O S dot com slash "
text+=ID[0]+" "+ID[1]+" "+ID[2]+" "+ID[3]

data = '--data "{\\"text\\":\\"'+text+'\\"}"'

apikey = 'PHhQmnTrwRXooRVU3TqF89IHEFckIetm1fSpW29JcOFX'
header1 = '--header "Content-Type: application/json"'
header2 = '--header "Accept: audio/wav"'
output = '--output '+ID+'.wav'
url = 'https://api.us-east.text-to-speech.watson.cloud.ibm.com/instances/d7354172-043b-4343-a958-80b8ba3acceb'

cmd = 'curl -X POST -u "apikey:'+apikey+'" '+header1+' '+header2+' '+data+' '+output+' "'+url+'/v1/synthesize"'
print(cmd)
os.system(cmd)

playsound(ID+".wav")
