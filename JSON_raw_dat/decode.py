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

## Retrieve all pertinant info from 
## the bundle of data we have recovered
def storeTrialTimes( allTrials ):
    #variables
    allTrialsStr = ""
    allTrialsFile = allTrials['subject'] + "_all_trials" + ".csv"
    errorOutFile = allTrials['subject'] + "_errors" + ".csv"
    keystrokeOutFile = allTrials['subject'] + "_keystrokes" + ".csv" 
    inChars = ",,"
    csvOut = ""
    fileout = ""
    errOut = ""
    keystrokeOut = ""
    acc = 0
    avg = 0
    errors = 0

    ###formats all but the first input
    ###for output & outputs
    for y in range(0, len(allTrials['data'])):
        fileout = (allTrials['subject'] + "-" + ("trial" + str(y)) + ".csv")
       
        #append the two strings showing intended
        #input and given input 
        csvOut = "\"" + allTrials['data'][y]['errors']['cmpStr'] + "\"" + ","
        csvOut += "\"" + allTrials['data'][y]['errors']['input'] + "\"" + ","
        
        errOut += "\"" + allTrials['data'][y]['errors']['cmpStr'] + "\"" + ","
        errOut += "\"" + allTrials['data'][y]['errors']['input'] + "\"" + ","
        
        keystrokeOut += "\"" + allTrials['data'][y]['errors']['cmpStr'] + "\"" + ","
        keystrokeOut += "\"" + allTrials['data'][y]['errors']['input'] + "\"" + ","
        
        #assign for readability
        inDat = allTrials['data'][y]['inputData']
        errDat = allTrials['data'][y]['errors']

        #inner loop grabs all the characters and
        #times for one trial forms strings for output
        for x in range(1, len(inDat)):
            inChars += inDat[x].get('letter', '\0').encode('utf-8') + ","
            csvOut = csvOut + (str(inDat[x]['time']) + ",")
            acc += inDat[x]['time']


        #compute average for given trial and append
        avg = acc/len(inDat)
        inChars += "\n"
        csvOut += "," + str(avg) + "\n"



        #form error Data
        errors = getErrors(allTrials['data'][y])
        errOut += str(errors) + ","
        errOut += str(errDat['errors']) + ","         #errors processed with a check of equality
        errOut += str(errors + errDat['errors']) + "\n"

        #form keystrokes data
        keystrokeOut += str(getKeyStrokes(inDat)) + "\n"

        #output errors and keystrokes
        outputCSV(errorOutFile, errOut.encode('utf-8'))
        outputCSV(keystrokeOutFile, keystrokeOut.encode('utf-8'))


        #append to all trials
        #for final cumulative output
#        allTrialsStr += csvOut.encode('utf-8')        
    

#        csvOut = inChars + csvOut.encode('utf-8')
#        outputCSV(fileout, csvOut)

        #reset values for next iter
        csvOut = ""
        inChars = ",,"
       # errOut = ""
       # keystrokeOut = ""
        acc = 0

    ##Output all trials
#    outputCSV(allTrialsFile, allTrialsStr)
    outputCSV(errorOutFile, errOut.encode('utf-8'))
    outputCSV(keystrokeOutFile, keystrokeOut.encode('utf-8'))

##Get info about errors from one trial
def getErrors(trialData):
    return (len(trialData['inputData']) - len(trialData['errors']['cmpStr']))

def getKeyStrokes(inputDat):
    return (len(inputDat))

##############################################
#Main Driver##################################
##############################################


###Variables##############


filename = raw_input('Please enter the filename to be decoded:')

##read in the json string
JSON_file = open(filename, 'r')
JSON_string = JSON_file.read()
JSON_file.close()

##convert JSON to python data structures
anima_data = json.loads(JSON_string)
subject = anima_data['subject']

storeTrialTimes(anima_data)















