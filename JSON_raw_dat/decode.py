#Filename : decode.py
#About: Takes a file holding a single json string and decodes it, stores it as a series of CSV

#external libraries
from __future__ import print_function
import json

csvOut = ""
fileout = ""

filename = raw_input('Please enter the filename to be decoded:')

#read in the json string
JSON_file = open(filename, 'r')
JSON_string = JSON_file.read()
JSON_file.close()

#convert to python data structures
anima_data = json.loads(JSON_string)

subject = anima_data['subject']

#retrieves all but the first input
for y in range(0, len(anima_data['data'])):
    fileout = (anima_data['subject'] + "-" + ("trial" + str(y)) + ".csv")
    for x in range(1, len(anima_data['data'][y]['inputData'])):
        csvOut = csvOut + (str(anima_data['data'][y]['inputData'][x]['time']) + ",")
    
    CSV_file = open(fileout, 'w')
    print(csvOut)
    CSV_file.write(csvOut)
    CSV_file.close()

    csvOut = ""
    






#print trial1

