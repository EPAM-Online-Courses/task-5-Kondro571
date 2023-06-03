package efs.task.collections.game;

import efs.task.collections.data.DataProvider;
import efs.task.collections.entity.Hero;
import efs.task.collections.entity.Town;



import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
public class GameLobby {

    public static final String HERO_NOT_FOUND = "Nie ma takiego bohatera ";
    public static final String NO_SUCH_TOWN = "Nie ma takiego miasta ";

    private final DataProvider dataProvider;
    private Map<Town, List<Hero>> playableTownsWithHeroesList;

    public GameLobby() {
        this.dataProvider = new DataProvider();
        this.playableTownsWithHeroesList =
                mapHeroesToStartingTowns(dataProvider.getTownsList(), dataProvider.getHeroesSet());
    }

    public Map<Town, List<Hero>> getPlayableTownsWithHeroesList() {
        return playableTownsWithHeroesList;
    }

    //TODO Dodać miasta i odpowiadających im bohaterów z DLC gry do mapy dostępnych
    // miast - playableTownsWithHeroesList, tylko jeżeli jeszcze się na niej nie znajdują.
    public void enableDLC() {
        List<Hero> heroesDLC = List.copyOf(dataProvider.getDLCHeroesSet());
        for(Town town: dataProvider.getDLCTownsList()){
            if(!playableTownsWithHeroesList.containsKey(town)){
                playableTownsWithHeroesList.put(town,heroesDLC);
            }
        }
    }


    //TODO Usunąć miasta i odpowiadających im bohaterów z DLC gry z mapy dostępnych
    // miast - playableTownsWithHeroesList.
    public void disableDLC() {

        for(Town town: dataProvider.getDLCTownsList()){
            if(playableTownsWithHeroesList.containsKey(town)) {
                playableTownsWithHeroesList.remove(town);
            }
        }
    }

    // TODO Sprawdza czy mapa playableCharactersByTown zawiera dane miasto.
    //  Jeśli tak zwróć listę bohaterów z tego miasta.
    //  Jeśli nie rzuć wyjątek NoSuchElementException z wiadomością NO_SUCH_TOWN + town.getName()
    public List<Hero> getHeroesFromTown(Town town) {

        if(playableTownsWithHeroesList.containsKey(town)){
            return playableTownsWithHeroesList.get(town);
        }else{
            throw new NoSuchElementException(NO_SUCH_TOWN  + town.getTownName());
        }
    }

    // TODO Metoda powinna zwracać mapę miast w kolejności alfabetycznej z odpowiadającymi im bohaterami.
    //  Każde z miast charakteryzuje się dwoma klasami bohaterów dostępnymi dla tego miasta - Town.startingHeroClass.
    //  Mapa ma zawierać pare klucz-wartość gdzie klucz: miasto, wartość: lista bohaterów;
    public Map<Town, List<Hero>> mapHeroesToStartingTowns(List<Town> availableTowns, Set<Hero> availableHeroes) {
        Map<Town, List<Hero>> townsWithHeroesMap = new TreeMap<>(Comparator.comparing(Town::getTownName));

        for(Town town : availableTowns){
            List<String> startingHeros = town.getStartingHeroClasses();
            List<Hero> heroesForTown = new ArrayList<>();

            for(Hero hero : availableHeroes){
                if(startingHeros.contains(hero.getHeroClass())){
                    heroesForTown.add(hero);
                }

            }
            townsWithHeroesMap.put(town,heroesForTown);
        }
        return townsWithHeroesMap;
    }

    //TODO metoda zwraca wybranego bohatera na podstawie miasta z którego pochodzi i imienia.
    // Jeżeli istnieje usuwa go z listy dostępnych bohaterów w danym mieście i zwraca bohatera.
    // Jeżeli nie ma go na liście dostępnych bohaterów rzuca NoSuchElementException z wiadomością HERO_NOT_FOUND + name
    public Hero selectHeroByName(Town heroTown, String name) {

        if(playableTownsWithHeroesList.containsKey(heroTown)){
            List<Hero> heroesList = playableTownsWithHeroesList.get(heroTown);
            for (Hero hero : heroesList) {
                if (hero.getName().equals(name)) {
                    heroesList.remove(hero);
                    return hero;
                }
            }

            throw new NoSuchElementException(HERO_NOT_FOUND  + name);
        }else{
            throw new NoSuchElementException(HERO_NOT_FOUND  + name);
        }

    }

   
}
