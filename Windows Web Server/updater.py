import glob
import os
from shutil import copyfile
from shutil import rmtree
import paramiko
import time
media_dir = "C:/inetpub/ftproot/FTP.Komza.com/Media/"
server_dir = "C:/inetpub/wwwroot/SOS.Komza.com/"
home_dir = "C:/Users/lkomza/Desktop/"
logged_files = []

while(True):
	os.chdir(media_dir) # search for media
	current_files = glob.glob('*.txt') + glob.glob('*.jpg')+ glob.glob('*.png')
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
				html[21] = html[21].replace("No information given.", desc[0])
				new_html = open(server_dir+ID+'/'+"index.html","w")
				new_html.writelines(html)
				new_html.close()
	for logged_file in logged_files:
		if(not logged_file in current_files):
			ID = logged_file.split('_')[-1].split('.')[0] # get ID
			print("File removed: "+logged_file)
			logged_files.remove(logged_file)
			if(ID in subdirs):
				print("Removing webpage with ID "+ID)
				rmtree(server_dir+ID)
				time.sleep(0.5)