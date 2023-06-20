package com.example.TourManager.Services;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import com.example.TourManager.Comparators.MostPopularCompare;
import com.example.TourManager.Comparators.MostVisitedCompare;
import com.example.TourManager.Comparators.DateSortCompare;
import com.example.TourManager.Comparators.MostFavoriteCompare;
import com.example.TourManager.Models.DateClass;
import com.example.TourManager.Models.ClassModels.Destination;
import com.example.TourManager.Models.ClassModels.Filter;
import com.example.TourManager.Models.ClassModels.FilterForCriteria;
import com.example.TourManager.Models.EntityModels.Landmark;
import com.example.TourManager.Models.EntityModels.User;
import com.example.TourManager.Models.Enums.NaturalTypes;
import com.example.TourManager.Models.Enums.Regions;
import com.example.TourManager.Repositories.LandmarkRepository;
import com.example.TourManager.Repositories.AuthRepositories.UserRepo;
import com.example.TourManager.Services.AuthServices.UserService;

import jakarta.mail.Flags.Flag;

@Service
public class LandmarkService {

    @Autowired
    private LandmarkRepository _landmarkRepository;

    @Autowired
    private UserService _userService;

    @Autowired
    private UserRepo _userRepository;

    public Filter pageFilter;

    public List<Landmark> filteredList;

    public FilterForCriteria currentFilterForCriteria;

    public List<Landmark> filteredListByCriteria;
    
    @Autowired
    public LandmarkService() {
        
    }

    public List<Landmark> GetAll(){
        List<Landmark> allLandmarks = this._landmarkRepository.findAll();
        return allLandmarks;
    }

    public Landmark GetById(String id){
        Optional<Landmark> entity = this._landmarkRepository.findById(id);
        if (entity.isPresent()) {
            return entity.get();
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Landmark with id: " + id + " is not found");
        }
    }

    public void create(Landmark landmark){
        this._landmarkRepository.save(landmark);
    }

    public void edit(Landmark landmark) {
        this._landmarkRepository.save(landmark);
    }

    public void Delete(String id){
        this._landmarkRepository.deleteById(id);
    }

    public void emptyFilter(){
        pageFilter = new Filter();
    }

    public boolean deleteFileFromDirectory(File directoryWithFiles) {
        File[] allContents = directoryWithFiles.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                return file.delete();
            }
        }
        return false; 
    }

    public boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete(); 
    }
        

    public void getAllEntitiesForNaturalType(NaturalTypes naturalType) {
        clearFilteredLists();
        removeFilters();
        if (naturalType != null) {     
            filteredList = this.GetAll()
                               .stream()
                               .filter(l -> l.naturalType != null)
                               .filter(l -> l.naturalType.equals(naturalType))
                               .collect(Collectors.toList());
        }
        pageFilter.type = "natural";
        pageFilter.value = naturalType.name().toLowerCase();
    }

    public void getAllEntitiesForHistoricalRegion(Regions historicalRegion) {
        clearFilteredLists();
        removeFilters();

        if (historicalRegion != null) {     
            filteredList = this.GetAll()
                               .stream()
                               .filter(l -> l.historicalRegion != null)
                               .filter(l -> l.historicalRegion.equals(historicalRegion))
                               .collect(Collectors.toList());
        }
        pageFilter.type = "historical";
        pageFilter.value = historicalRegion.name().toLowerCase();
    }

    public void clearFilteredLists(){
        filteredList = this.GetAll();
        this.filteredListByCriteria.clear();
    }

    public void createFilter() {
        pageFilter = new Filter("", "");
    }

    public void removeFilters() {
        pageFilter.type = "";
        pageFilter.value = "";
        currentFilterForCriteria.value = "";
    }

    public void addToFavorites(String id) {
        var landmark = this.GetById(id);

        int countOfEqualFavorites = 0;
        User user = _userService.getCurrentUser();
        
        if (!user.favoriteLandmarks.isEmpty()) {
            for (Landmark objLand : user.favoriteLandmarks) {
                if (objLand.id.equals(landmark.id)) {
                    countOfEqualFavorites++;               
                }
                
                if (countOfEqualFavorites > 0) {
                    user.favoriteLandmarks.removeAll(Arrays.asList(objLand));
                    break;
                }
            }
        }

        if (landmark.views != 0) {
            landmark.views--;
            _landmarkRepository.save(landmark);  
        }

        if (countOfEqualFavorites == 0) {
            user.favoriteLandmarks.add(landmark);
        }
        this._userRepository.save(user);
        
        /* 
        if(countOfEqualFavoriteDeals == 0 && userItems == 0 && !haveFavoritesForUser){
            userEntity.favoriteLandmarks = new ArrayList<>();
            userEntity.favoriteLandmarks.add(landmark);
        }
        else if (countOfEqualFavoriteDeals == 0) {
            userEntity.favoriteLandmarks.add(landmark);
            int i = 0;
        }
        this._userRepository.save(userEntity);*/     
    }

    public boolean isLandmarkFavoriteForUser(Landmark landmark) {
        var user = _userService.getCurrentUser();
        if (!user.favoriteLandmarks.isEmpty()) {
            for (Landmark land : user.favoriteLandmarks) {
                if (land.id.equals(landmark.id)) {
                    return true;
                }
            }    
        }
        return false;
    }

    /*public boolean isLandmarkWantToVisitForUser(Landmark landmark) {
        var user = _userService.getCurrentUser();
        if (!user.destinations.isEmpty()) {
            for (Destination dest : user.destinations) {
                if (dest.destionationId.equals(landmark.id)) {
                    if (dest.userWantToVisit == true) {
                        return true;      
                    }
                }
            }
        }
        return false;
    }*/

    public boolean addDestinationForUserWhoWantToVisit(Landmark landmark, User user, boolean landmarkChanged) {
        Destination destination = createDestination(landmark.id, landmark.name);
        destination.userWantToVisit = true;
        landmark.numberOfPeopleWhoWantToVisit++;
        landmarkChanged = true;
        user.destinations.add(destination);
        return landmarkChanged;
    }

    public void setUserWasHereForLandmark(String id) {
        boolean landmarkChanged = false;
        var landmark = this.GetById(id);
        var user = _userService.getCurrentUser();
        if (user.destinations.isEmpty()) {
            landmarkChanged = this.addDestinationForUserWhoWasHere(landmark, user, landmarkChanged);    
        }
        else {
            for (Destination dest : user.destinations) {
                if (dest.destionationId.equals(landmark.id)) {
                    dest.userWasHere = !dest.userWasHere;
                    if (dest.userWasHere) {
                        landmark.numberOfPeopleToBeThere++;
                        landmarkChanged = true;
                    }
                    else {
                        if (landmark.numberOfPeopleToBeThere != 0) {
                            landmark.numberOfPeopleToBeThere--; 
                            landmarkChanged = true;        
                        }
                    }  
                    
                    if (!dest.userWantToVisit && !dest.userWasHere) {
                        user.destinations.removeAll(Arrays.asList(dest));
                        break;
                    }
                }
            }

            if (!landmarkChanged) {
                landmarkChanged = this.addDestinationForUserWhoWasHere(landmark, user, landmarkChanged);
            }
        }

        if (landmark.views != 0) {
            landmark.views--;   
        }

        if (landmarkChanged) {
            _landmarkRepository.save(landmark);        
        }

        _userRepository.save(user); 
    }

    public void setUserWantToVisitLandmark(String id) {
        boolean landmarkChanged = false;
        var landmark = this.GetById(id);
        var user = _userService.getCurrentUser();
        if (user.destinations.isEmpty()) {
            landmarkChanged = this.addDestinationForUserWhoWantToVisit(landmark, user, landmarkChanged);    
        }
        else {
            for (Destination dest : user.destinations) {
                if (dest.destionationId.equals(landmark.id)) {
                    dest.userWantToVisit = !dest.userWantToVisit;
                    if (dest.userWantToVisit) {
                        landmark.numberOfPeopleWhoWantToVisit++;
                        landmarkChanged = true;
                    }
                    else {
                        if (landmark.numberOfPeopleWhoWantToVisit != 0) {
                            landmark.numberOfPeopleWhoWantToVisit--;
                            landmarkChanged = true;
                        }
                    }

                    if (!dest.userWantToVisit && !dest.userWasHere) {
                        user.destinations.removeAll(Arrays.asList(dest));
                        break;
                    }
                }
            }

            if (!landmarkChanged) {
                landmarkChanged = this.addDestinationForUserWhoWantToVisit(landmark, user, landmarkChanged);
            }
        }
        
        if (landmark.views != 0) {
            landmark.views--;   
        }

        if (landmarkChanged) {
            _landmarkRepository.save(landmark);        
        }
        
        _userRepository.save(user);
    }

    public boolean wasUserHereForLandmark(Landmark landmark) {
        var user = _userService.getCurrentUser();
        if (!user.destinations.isEmpty()) {
            for (Destination dest : user.destinations) {
                if (dest.destionationId.equals(landmark.id)) {
                    return dest.userWasHere;
                }
            }
        }
        return false;
    }

    public boolean wantUserToVisitLandmark(Landmark landmark) {
        var user = _userService.getCurrentUser();
        if (!user.destinations.isEmpty()) {
            for (Destination dest : user.destinations) {
                if (dest.destionationId.equals(landmark.id)) {
                    return dest.userWantToVisit;
                }
            }
        }
        return false;
    }

    public Destination createDestination(String destinationId, String destinationName) {
        Destination destination = new Destination();
        destination.destionationId = destinationId;
        destination.destinationName = destinationName;
        destination.userWantToVisit = false;
        destination.userWasHere = false;
        return destination;
    }

    public boolean addDestinationForUserWhoWasHere(Landmark landmark, User user, boolean landmarkChanged) {
        Destination destination = createDestination(landmark.id, landmark.name);
        destination.userWasHere = true;
        landmark.numberOfPeopleToBeThere++;
        landmarkChanged = true;
        user.destinations.add(destination);
        return landmarkChanged;
    }
    
    /*public Destination addDestinationForUserWhoWantToVisit(Landmark landmark, User user) {

        if (!haveEqualDestinationsForCurrentUser(landmark, user)) {
            Destination destination = createDestination(landmark.id, landmark.name);
            destination.userWantToVisit = true;
            user.destinations.add(destination);
            return destination;    
        }
        else {
            return new Destination(landmark.id, landmark.name, false, false);
        }
    }*/

    public boolean haveEqualDestinationsForCurrentUser(Landmark landmark, User user){
            for (Destination dest : user.destinations) {
                if (dest.destionationId.equals(landmark.id)) {
                    return true;
                }
            }
            return false;
    }

    public Collection<Landmark> getFavoritesForUser() {
        var user = _userService.getCurrentUser();

        Collection<Landmark> favLandmarks = new ArrayList<>();
        if (!user.favoriteLandmarks.isEmpty()) {
            Iterator<Landmark> it = user.favoriteLandmarks.iterator();
            while(it.hasNext()) {
                favLandmarks.add(_landmarkRepository.findById(it.next().id).get());
            }
            return favLandmarks;
        }
        else {
            return Collections.emptyList();
        }
    }

    public Collection<Landmark> getWantToVisitLandmarksForUser() {
        var user = _userService.getCurrentUser();

        if (!user.destinations.isEmpty()) {
            Collection<Landmark> destinationsForUser = new ArrayList<>();
            for (Destination destination : user.destinations.stream().filter(dest -> dest.userWantToVisit == true).collect(Collectors.toList())) {
                Optional<Landmark> landmark = _landmarkRepository.findById(destination.destionationId);
                if (landmark.isPresent()) {
                    destinationsForUser.add(landmark.get());
                }
            }
            return destinationsForUser;
        }
        else {
            return Collections.emptyList();
        }
    }

    public Collection<Landmark> getWasOnLandmarksForUser() {
        var user = _userService.getCurrentUser();

        if (!user.destinations.isEmpty()) {
            Collection<Landmark> destinationsForUser = new ArrayList<>();
            for (Destination destination : user.destinations.stream().filter(dest -> dest.userWasHere == true).collect(Collectors.toList())) {
                Optional<Landmark> landmark = _landmarkRepository.findById(destination.destionationId);
                if (landmark.isPresent()) {
                    destinationsForUser.add(landmark.get());
                }
            }
            return destinationsForUser;
        }
        else {
            return Collections.emptyList();
        }
    }

    public void filterBySelectedCriteria(String filterCriteria) {
        currentFilterForCriteria.value = filterCriteria;
        this.filteredListByCriteria.clear();

        if (filterCriteria.equals("popular")) {
            /*SortedSet<Landmark> sortedLandmarksByMostPopular = new TreeSet<>(Comparator.comparingInt(landmark -> landmark.views));
            sortArray(sortedLandmarksByMostPopular);*/
            ArrayList<Landmark> sortedLandmarksByMostPopular = new ArrayList<>();
            for (Landmark landmark : filteredList) {
                filteredListByCriteria.add(landmark);
            }
            Collections.sort(filteredListByCriteria, new MostPopularCompare());
        }
        else if (filterCriteria.equals("visited")) {
            /*SortedSet<Landmark> sortedLandmarksByMostVisited = new TreeSet<>(Comparator.comparingInt(landmark -> landmark.numberOfPeopleToBeThere));
            sortArray(sortedLandmarksByMostVisited);*/
            for (Landmark landmark : filteredList) {
                filteredListByCriteria.add(landmark);
            }
            Collections.sort(filteredListByCriteria, new MostVisitedCompare());
        }
        else if (filterCriteria.equals("new")) {
            ArrayList<DateClass> dates = new ArrayList<>();
            for (Landmark landmark : filteredList) {
                dates.add(new DateClass(landmark.dateOfOffer, landmark));
            }
            Collections.sort(dates, new DateSortCompare());

            for (DateClass date : dates) {
                filteredListByCriteria.add(date.landmark);
            }
        }
        else if (filterCriteria.equals("favorite")) {
            /*SortedSet<Landmark> sortedLandmarksByMostFavorite = new TreeSet<>(Comparator.comparingInt(landmark -> landmark.rating));
            sortArray(sortedLandmarksByMostFavorite);*/
            ArrayList<Landmark> sortedLandmarksByMostFavorite = new ArrayList<>();
            for (Landmark landmark : filteredList) {
                filteredListByCriteria.add(landmark);
            }
            Collections.sort(filteredListByCriteria, new MostFavoriteCompare());
        }
    }

   /* public void sortArray(SortedSet<Landmark> array) {
        Iterator<Landmark> it = filteredList.iterator();
        while(it.hasNext()){
            array.add(it.next());
        }

        for (Landmark land : filteredList) {
            array.add(land);
        }
        filteredListByCriteria = new ArrayList<>(array);
        Collections.reverse(filteredListByCriteria);
    } */
}
