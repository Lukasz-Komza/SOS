import glob
import os
from shutil import copyfile
from shutil import rmtree
import paramiko
import json
import time
media_dir = "C:/inetpub/ftproot/FTP.Komza.com/Media/"
server_dir = "C:/inetpub/wwwroot/SOS.Komza.com/"
tts_dir = "C:/inetpub/ftproot/FTP.Komza.com/TTS/"
home_dir = "C:/Users/lkomza/Desktop/"
logged_files = []

while(True):
	os.chdir(media_dir) # search for media
	current_files = glob.glob('*.txt')+glob.glob('*.jpg')+glob.glob('*.png')+glob.glob('*.json')
	subdirs = glob.glob(server_dir+"*/")
	subdirs = [d.split('\\')[-2] for d in subdirs]
	# if a new file is found
	for file in current_files:
		if(not file in logged_files):
			print("Found new file: "+file)
			time.sleep(0.5)
			os.chdir(server_dir)
			ID = file.split('_')[-1].split('.')[0] # get ID
			# if a webpage doesn't currently exist, make one
			if(not ID in subdirs):
				print("Creating new webpage with ID "+ID)
				os.mkdir(server_dir+ID)
				os.mkdir(server_dir+ID+"/Resources")
				os.mkdir(server_dir+ID+"/Resources/Images")
				copyfile(home_dir+"t.html",server_dir+ID+"/index.html")
				copyfile(home_dir+"t.js",server_dir+ID+"/SOSmaps.js")
			if(file.split('.')[-1] == 'png' or file.split('.')[-1] == 'jpg'):
				logged_files.append(file)
				new_name = file.split('_')[0]+'.'+file.split('.')[-1]
				copyfile(media_dir+file,server_dir+ID+'/Resources/Images/'+new_name)
			if(file.split('.')[-1] == 'txt'):
				logged_files.append(file)
				html_file = open(server_dir+ID+"/index.html","r")
				html = html_file.readlines()
				html_file.close()
				desc_file = open(media_dir+file)
				desc = desc_file.readlines()
				desc_file.close()
				html[25] = html[25].replace("<!--desc-->", desc[0])
				new_html = open(server_dir+ID+'/'+"index.html","w")
				new_html.writelines(html)
				new_html.close()
			if(file.split('.')[-1] == 'json'):
				logged_files.append(file)
				with open(media_dir+ID+".json") as j_file: data = json.load(j_file)
				try:
					lat = data['Location']['lat']
					lon = data['Location']['lon']
					js_file = open(server_dir+ID+"/SOSmaps.js","r")
					js = js_file.readlines()
					js_file.close()
					js[3] = js[3].replace("0.1",lat)
					js[3] = js[3].replace("0.2",lon)
					new_js = open(server_dir+ID+'/'+"SOSmaps.js","w")
					new_js.writelines(js)
					new_js.close()
				except:
					pass
				user_keys = list(data['User'].keys())
				user_info = [data['User'][key] for key in user_keys]
				user_data = ""
				for i in range(len(user_keys)):
					user_data+=user_keys[i]+": "+user_info[i]+"<br>"
				html_file = open(server_dir+ID+"/index.html","r")
				html = html_file.readlines()
				html_file.close()
				html[30] = html[30].replace("<!--data-->", user_data)
				emergency_type = data['Emergency']
				f_txt = ""
				if(emergency_type == 'Fire'):
					html[17] = '  <h4 style="width:15%; color:lightgray; background-color:#FF1D1D; border-radius: 12px; text-align: center; margin-left:auto; margin-right:auto; font-size: 24px;">Fire</h4>'
					f_txt = "a fire-related emergency."
				if(emergency_type == 'Police'):
					html[17] = '  <h4 style="width:15%; color:lightgray; background-color: blue; border-radius: 12px; text-align: center; margin-left:auto; margin-right:auto; font-size: 24px;">Police</h4>'
					f_txt = "an emergency requiring Police assistance."
				if(emergency_type == 'EMS'):
					html[17] = '  <h4 style="width:15%; color:lightgray; background-color: green; border-radius: 12px; text-align: center; margin-left:auto; margin-right:auto; font-size: 24px;">EMS</h4>'
					f_txt = "an issue requiring emergency medical services."
				html[17]+="\n"
				new_html = open(server_dir+ID+"/index.html","w")
				new_html.writelines(html)
				new_html.close()
				if(data['tts_true']=='true'):
					raw_tts = "I am experiencing "+f_txt
					tts_file = open(tts_dir+ID+".txt","w")
					tts_file.writelines(raw_tts)
					tts_file.close()

	for logged_file in logged_files:
		if(not logged_file in current_files):
			ID = logged_file.split('_')[-1].split('.')[0] # get ID
			print("File removed: "+logged_file)
			logged_files.remove(logged_file)
			if(ID in subdirs):
				print("Removing webpage with ID "+ID)
				rmtree(server_dir+ID)
				time.sleep(0.5)
