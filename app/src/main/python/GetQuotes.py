import pandas as pd
from urllib.request import Request, urlopen
from textblob import TextBlob

def get_quotes():
    url='https://drive.google.com/uc?id=1NHEP8co2TjqGq4zZN0-F2wHxDSDdFTkD&export=download'
    s=Request(url)
    s.add_header('User-Agent','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.76 Safari/537.36')
    content=urlopen(s)
    dataframe=pd.read_csv(content)
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
    url='https://drive.google.com/uc?id=1NHEP8co2TjqGq4zZN0-F2wHxDSDdFTkD&export=download'
    s=Request(url)
    s.add_header('User-Agent','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.76 Safari/537.36')
    content=urlopen(s)
    dataframe=pd.read_csv(content)
    arr=dataframe['Votes'].values
    arr[ind]=arr[ind]+1
    dataframe['Votes']=arr
    dataframe.to_csv(r'https://drive.google.com/file/d/1NHEP8co2TjqGq4zZN0-F2wHxDSDdFTkD/view?usp=sharing')

def dislike_quote(ind):
    url='https://drive.google.com/uc?id=1NHEP8co2TjqGq4zZN0-F2wHxDSDdFTkD&export=download'
    s=Request(url)
    s.add_header('User-Agent','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.76 Safari/537.36')
    content=urlopen(s)
    dataframe=pd.read_csv(content)
    arr=dataframe['Votes'].values
    arr[ind]=arr[ind]-1
    dataframe['Votes']=arr
    dataframe.to_csv(r'https://drive.google.com/file/d/1NHEP8co2TjqGq4zZN0-F2wHxDSDdFTkD/view?usp=sharing')