import pandas as pd
mymemes=[]
mygames=[]
mymovies=[]
gamenames=[]
movienames=[]
def get_memes(memeliststr):
    retmemes=' '
    memelist=memeliststr.split(' ')
    dataframe=pd.read_csv("https://drive.google.com/u/0/uc?id=1e020BmxQkRAGBP3vuTjfqy2jlINFhFB8&export=download")
    for meme_category in memelist:
        newdf=dataframe[dataframe['category']==meme_category]
        mymemes.extend(list(newdf['link']))
    for x in mymemes:
        retmemes=retmemes+x+' '
    retmemes=retmemes.strip()
    return retmemes
def get_games(gameliststr):
    retgames=' '
    gamelist=gameliststr.split('@')
    dataframe=pd.read_csv("https://drive.google.com/u/0/uc?id=1mBpmdQBklySD-KglB6PZ2jk85GJOBKij&export=download")
    for game_category in gamelist:
        newdf=dataframe[dataframe['category']==game_category]
        gamenames.extend(list(newdf['name']))
        mygames.extend(list(newdf['link']))
    for i in range(len(mygames)):
        retgames=retgames+gamenames[i]+'@'+mygames[i]+'@'
    retgames=retgames[0:len(retgames)-1]
    return retgames
def get_movies(movieliststr):
    retmovies=' '
    movielist=movieliststr.split('@')
    dataframe=pd.read_csv("https://drive.google.com/u/0/uc?id=1PiMQW9A_TuYmV20hIf3UhWgPuB4W0zro&export=download")
    for movie_category in movielist:
        newdf=dataframe[dataframe['genre1']==movie_category]
        movienames.extend(list(newdf['movie']))
        mymovies.extend(list(newdf['platform']))
    for i in range(len(mymovies)):
        retmovies=retmovies+movienames[i]+'@'+mymovies[i]+'@'
    retmovies=retmovies[0:len(retmovies)-1]
    return retmovies