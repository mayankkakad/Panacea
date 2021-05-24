import pandas as pd
import numpy as np
from sklearn.tree import DecisionTreeClassifier
from apyori import apriori

def preprocessing(dataframe):
    memes=[]
    games=[]
    movies=[]
    for i in range(len(dataframe)):
        temp=[]
        temp=dataframe.values[i,7].split(";")
        for j in range(len(temp)):
            temp[j]=temp[j].strip()
        memes.append(temp)
    for i in range(len(dataframe)):
        temp=[]
        temp=dataframe.values[i,8].split(";")
        for j in range(len(temp)):
            temp[j]=temp[j].strip()
        games.append(temp)
    for i in range(len(dataframe)):
        temp=[]
        temp=dataframe.values[i,9].split(";")
        for j in range(len(temp)):
            temp[j]=temp[j].strip()
        movies.append(temp)
    return dataframe,memes,games,movies

def new_dataset(dataframe,memes,games,movies):
    memecol=[]
    gamecol=[]
    moviecol=[]
    for i in range(len(memes)):
        memecol.append(memes[i][0])
        gamecol.append(games[i][0])
        moviecol.append(movies[i][0])
    memearr=np.array(memecol)
    gamearr=np.array(gamecol)
    moviearr=np.array(moviecol)
    newdf=dataframe
    newdf['Memes']=memearr
    newdf['Games']=gamearr
    newdf['Movies/Series']=moviearr
    return newdf

def runApriori(memedict,gamedict,moviedict,memes,games,movies):
    minsup=0.15
    mincon=0.9
    minlift=2
    assoc_rules_memes=list(apriori(memes,min_support=minsup,min_confidence=mincon,min_lift=minlift))
    assoc_rules_games=list(apriori(games,min_support=minsup,min_confidence=mincon,min_lift=minlift))
    assoc_rules_movies=list(apriori(movies,min_support=minsup,min_confidence=mincon,min_lift=minlift))
    for i in assoc_rules_memes:
        sets=list(i[0])
        temp=list(memedict[str(sets[0])])
        temp.extend(sets[1:])
        temp=list(set(temp))
        memedict[sets[0]]=temp
    for i in assoc_rules_games:
        sets=list(i[0])
        temp=list(gamedict[str(sets[0])])
        temp.extend(sets[1:])
        temp=list(set(temp))
        gamedict[sets[0]]=temp
    for i in assoc_rules_movies:
        sets=list(i[0])
        temp=list(moviedict[str(sets[0])])
        temp.extend(sets[1:])
        temp=list(set(temp))
        moviedict[sets[0]]=temp
    return memedict,gamedict,moviedict

def predict_content(mood_points):
    moods=['1. Anxiety','2. Anger','3. Hopelessness','4. Perpetual/Long-term Boredom / Tiredness','5. Unreasonable/Unexplained Sadness']
    memedict={'Doggo':[],'Bollywood':[],'Food':[],'Sports':[],'Travel':[],'Political':[],'Dark':[]}
    gamedict={'Action':[],'Multiplayer':[],'Arcade':[],'Sports':[],'Racing':[],'Puzzle':[],'Adventure':[]}
    moviedict={'Drama':[],'Comedy':[],'Horror':[],'Action':[],'Romance':[],'Science fiction':[],'Animation':[],'Thriller':[],'Crime':[],'Biography':[]}
    dataframe=pd.read_csv('https://raw.githubusercontent.com/mayankkakad/datasets/main/mbcdataset.csv')
    dataframe,memes,games,movies=preprocessing(dataframe)
    newdf=new_dataset(dataframe,memes,games,movies)
    X=newdf[moods].values
    Y_meme=newdf['Memes'].values
    Y_game=newdf['Games'].values
    Y_movie=newdf['Movies/Series'].values
    memeTree=DecisionTreeClassifier()
    gameTree=DecisionTreeClassifier()
    movieTree=DecisionTreeClassifier()
    memeTree.fit(X,Y_meme)
    gameTree.fit(X,Y_game)
    movieTree.fit(X,Y_movie)
    predictedMeme=memeTree.predict(mood_points)
    predictedGame=gameTree.predict(mood_points)
    predictedMovie=movieTree.predict(mood_points)
    memedict,gamedict,moviedict=runApriori(memedict,gamedict,moviedict,memes,games,movies)
    predictedMemes=list(memedict[predictedMeme[0]])+list(predictedMeme)
    predictedGames=list(gamedict[predictedGame[0]])+list(predictedGame)
    predictedMovies=list(moviedict[predictedMovie[0]])+list(predictedMovie)
    return predictedMemes,predictedGames,predictedMovies

def get_content(a,b,c,d,e):
	mp=[[a,b,c,d,e]]
	mood_points=pd.DataFrame(mp).values
	mymemes,mygames,mymovies=predict_content(mood_points)
	finalresult=str(mymemes)+str(mygames)+str(mymovies)
	return finalresult