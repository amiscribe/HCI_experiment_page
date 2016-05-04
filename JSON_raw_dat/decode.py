#Filename : decode.py
#About: Takes a file holding a single json string and decodes it, stores it as a series of CSV

#external libraries
from __future__ import print_function
import json


#function definitions


#outputs data expressed as comma seperated values
def outputCSV(filename, output): 
    CSV_file = open(filename, 'w')
    CSV_file.write(output)
    CSV_file.close()

def storeTrialTimes( allTrials ):
    #variables
    allTrialsStr = ""
    allTrialsFile = allTrials['subject'] + "_all_trials" + ".csv"
    csvOut = ""
    fileout = ""
    acc = 0
    avg = 0
    
    ###formats all but the first input
    ###for output & outputs
    for y in range(0, len(allTrials['data'])):
        fileout = (allTrials['subject'] + "-" + ("trial" + str(y)) + ".csv")
        csvOut = "Trial" + str(y) + ","

        inDat = allTrials['data'][y]['inputData']

        for x in range(1, len(inDat)):
            csvOut = csvOut + (str(inDat[x]['time']) + ",")
            acc += inDat[x]['time']
    
        #compute average for given trial and append
        avg = acc/len(inDat)
        csvOut += str(avg) + "\n"

        #append to all trials
        allTrialsStr += csvOut        

        outputCSV(fileout, csvOut)

        csvOut = ""
    
    ##Output all trials
    outputCSV(allTrialsFile, allTrialsStr)



##############################################
#Main Driver##################################
##############################################

###Variables################


filename = raw_input('Please enter the filename to be decoded:')

#read in the json string
JSON_file = open(filename, 'r')
JSON_string = JSON_file.read()
JSON_file.close()

#convert to python data structures
anima_data = json.loads(JSON_string)

subject = anima_data['subject']

storeTrialTimes(anima_data)

#print trial1

