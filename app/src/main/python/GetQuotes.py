import pandas as pd
from urllib.request import Request, urlopen
from textblob import TextBlob

def get_quotes():
    dataframe=pd.read_csv('https://raw.githubusercontent.com/mayankkakad/datasets/main/quotedataset.csv')
    quoteslist=list(dataframe['Quote'])
    authorslist=list(dataframe['Author'])
    finalstring=''
    for i in range(len(quoteslist)):
        tempblob=TextBlob(quoteslist[i])
        if(tempblob.sentiment.polarity>0):
            finalstring=finalstring+quoteslist[i]+'@'+authorslist[i]+'@'
    finalstring=finalstring[0:len(finalstring)-1]
    return finalstring  

def like_quote(ind):
    dataframe=pd.read_csv('https://raw.githubusercontent.com/mayankkakad/datasets/main/quotedataset.csv')
    arr=dataframe['Votes'].values
    arr[ind]=arr[ind]+1
    dataframe['Votes']=arr
    dataframe.to_csv(r'https://raw.githubusercontent.com/mayankkakad/datasets/main/quotedataset.csv')

def dislike_quote(ind):
    dataframe=pd.read_csv('https://raw.githubusercontent.com/mayankkakad/datasets/main/quotedataset.csv')
    arr=dataframe['Votes'].values
    arr[ind]=arr[ind]-1
    dataframe['Votes']=arr
    dataframe.to_csv(r'https://raw.githubusercontent.com/mayankkakad/datasets/main/quotedataset.csv')