package com.example.TourManager.Controllers;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.example.TourManager.FileUploadUtils;
import com.example.TourManager.Models.ClassModels.FileDB;
import com.example.TourManager.Models.ClassModels.FilterForCriteria;
import com.example.TourManager.Models.EntityModels.Landmark;
import com.example.TourManager.Models.Enums.NaturalTypes;
import com.example.TourManager.Models.Enums.Places;
import com.example.TourManager.Models.Enums.Regions;
import com.example.TourManager.Models.Enums.TypesOfLandmark;
import com.example.TourManager.Repositories.LandmarkRepository;
import com.example.TourManager.Services.LandmarkService;
import com.example.TourManager.Services.UploadImageService;
import com.example.TourManager.Services.AuthServices.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/offers")
public class LandmarkController {

    @Autowired
    private UploadImageService _imageService;

    @Autowired
    private LandmarkRepository _landmarkRepository;

    @Autowired
    private UserService _userService;

    private LandmarkService _landmarkService;
    
    private final String mainControllerUrl = "http://localhost:8080/offers";

    
    public LandmarkController(LandmarkService landmarkService) {
        this._landmarkService = landmarkService;
        _landmarkService.createFilter();
        _landmarkService.filteredList = _landmarkService.GetAll();

        _landmarkService.currentFilterForCriteria = new FilterForCriteria();
        _landmarkService.filteredListByCriteria = new ArrayList<>();
    }

    @GetMapping
    public String getAllLandmarks(Model model, HttpServletRequest request) {


        if (_landmarkService.filteredListByCriteria.isEmpty()) {
            model.addAttribute("landmarkEntities", _landmarkService.filteredList);
            model.addAttribute("hasNoLandmarks", _landmarkService.filteredList.isEmpty());    
        }
        else {
            model.addAttribute("landmarkEntities", _landmarkService.filteredListByCriteria);
            model.addAttribute("filterCriteria", _landmarkService.currentFilterForCriteria.value);
            model.addAttribute("hasNoLandmarks", _landmarkService.filteredListByCriteria.isEmpty());
        }

        if (_landmarkService.pageFilter.type.equals("historical")) {
            model.addAttribute("title", "Исторически, " + _landmarkService.pageFilter.value.replace("_", " "));
            model.addAttribute("isHistorical", true);
        }
        else if (_landmarkService.pageFilter.type.equals("natural")) {
            model.addAttribute("title", "Природни, " + _landmarkService.pageFilter.value.replace("_", " "));
            model.addAttribute("isHistorical", false);
        }

        model.addAttribute("isSignedIn", _userService.hasUserLoggedIn());

        if (_userService.hasUserLoggedIn()) {
            model.addAttribute("isAdmin", _userService.isAdmin());
            model.addAttribute("hasAnyFavorites", !_userService.getCurrentUser().favoriteLandmarks.isEmpty());
            model.addAttribute("wantToVisitDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWantToVisit == true).findAny().isPresent());
            model.addAttribute("wasOnDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWasHere == true).findAny().isPresent());
        }
        else {
            model.addAttribute("hasAnyFavorites", false);
        }
        return "offers_page.html";
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String getEntityById(@PathVariable String id, Model model, HttpServletRequest request){
        
        Landmark landmark = this._landmarkService.GetById(id);
        var currentUser = _userService.getCurrentUser();

        if (_userService.getCurrentUser() == null) {
            model.addAttribute("isLandmarkFavoriteForUser", false);
            model.addAttribute("wasUserHereForLandmark", false);
            model.addAttribute("wantUserToVisitLandmark", false);
            model.addAttribute("hasAnyFavorites", false);
        }
        else {
            model.addAttribute("isLandmarkFavoriteForUser", _landmarkService.isLandmarkFavoriteForUser(landmark));
            model.addAttribute("wasUserHereForLandmark", _landmarkService.wasUserHereForLandmark(landmark));
            model.addAttribute("wantUserToVisitLandmark", _landmarkService.wantUserToVisitLandmark(landmark));
            model.addAttribute("hasAnyFavorites", !_userService.getCurrentUser().favoriteLandmarks.isEmpty());
            model.addAttribute("wantToVisitDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWantToVisit == true).findAny().isPresent());
            model.addAttribute("wasOnDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWasHere == true).findAny().isPresent());


            if (!_userService.isAdmin()) {
            landmark.views++;      
            _landmarkService.edit(landmark);
            }
        }
        

        model.addAttribute("landmark", landmark);
        model.addAttribute("isSignedIn", _userService.hasUserLoggedIn());

        model.addAttribute("isAdmin", _userService.isAdmin());

        return "single_offer_page.html";
    }

    @PostMapping("/region/filter")
    public RedirectView filterHistoricalRegion(Model model, 
                                            @RequestParam("historicalRegion") Regions historicalRegion,
                                            HttpServletRequest request){
        _landmarkService.getAllEntitiesForHistoricalRegion(historicalRegion); 

        return redirectView(mainControllerUrl);
    }
    
    @PostMapping("/type/filter")
    public RedirectView filterNaturalType(Model model, 
                                      @RequestParam("naturalType") NaturalTypes naturalType,
                                      HttpServletRequest request){
        _landmarkService.getAllEntitiesForNaturalType(naturalType);
        //_carService.setLanguage(request, model);
        

        return redirectView(mainControllerUrl);
    }

    @PostMapping("/filter/criteria")
    public RedirectView filterFinalListOfLandmarks(Model model, 
                                      @RequestParam("filterCriteria") String filterCriteria,
                                      HttpServletRequest request){

        _landmarkService.filterBySelectedCriteria(filterCriteria);

        return redirectView(mainControllerUrl);
    }
    
    @GetMapping("/favorites")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String getAllFavorites(Model model, HttpServletRequest request){
        
        Collection<Landmark> favLandmarks = _landmarkService.getFavoritesForUser();
        model.addAttribute("favorites", favLandmarks);
        
        model.addAttribute("isAdmin", _userService.isAdmin());
        model.addAttribute("wantToVisitDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWantToVisit == true).findAny().isPresent());
            model.addAttribute("wasOnDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWasHere == true).findAny().isPresent());
        //model.addAttribute("hasAnyFavorites", !_userService.getCurrentUser().favoriteLandmarks.isEmpty());
        
        //_carService.setLanguage(request, model);

        return "favorites_list.html";
    }

    @GetMapping("/wantToVisit/destinations")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String getAllDestinationsThatUserWantToVisit(Model model, HttpServletRequest request){
        
        Collection<Landmark> wantToVisitLandmarks = _landmarkService.getWantToVisitLandmarksForUser();
        model.addAttribute("wantToVisitLandmarks", wantToVisitLandmarks);
        
        model.addAttribute("isAdmin", _userService.isAdmin());
        model.addAttribute("hasAnyFavorites", !_userService.getCurrentUser().favoriteLandmarks.isEmpty());
        model.addAttribute("wasOnDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWasHere == true).findAny().isPresent());

        //model.addAttribute("wantToVisitDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWantToVisit == true).findAny().isPresent());
        
        //_carService.setLanguage(request, model);

        return "wantToVisit_list.html";
    }

    @GetMapping("/wasOn/destinations")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String getAllDestinationsThatUserWasOn(Model model, HttpServletRequest request){
        
        Collection<Landmark> wasOnLandmarks = _landmarkService.getWasOnLandmarksForUser();
        model.addAttribute("wasOnLandmarks", wasOnLandmarks);
        
        model.addAttribute("isAdmin", _userService.isAdmin());
        model.addAttribute("hasAnyFavorites", !_userService.getCurrentUser().favoriteLandmarks.isEmpty());
        model.addAttribute("wantToVisitDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWantToVisit == true).findAny().isPresent());
        //model.addAttribute("wasOnDestinations", _userService.getCurrentUser().destinations.stream().filter(dest -> dest.userWasHere == true).findAny().isPresent());
        //_carService.setLanguage(request, model);

        return "wasHere_list.html";
    }

    @PostMapping("/addToFavorites/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public RedirectView addToFavorites(@PathVariable String id, HttpServletRequest request){

        _landmarkService.addToFavorites(id);         

        return redirectView(mainControllerUrl + "/" + id);
    }


    @PostMapping("/userWantToVisit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public RedirectView userWantToVisit(@PathVariable String id){
            
        _landmarkService.setUserWantToVisitLandmark(id);          

        return redirectView(mainControllerUrl + "/" + id);
    }

    @PostMapping("/userWasHere/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public RedirectView userWasHere(@PathVariable String id){

         _landmarkService.setUserWasHereForLandmark(id);    

        return redirectView(mainControllerUrl + "/" + id);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RedirectView create(String name, Places place, TypesOfLandmark type, 
                               Regions historicalRegion, NaturalTypes naturalType,
                               String description, Integer rating, @RequestParam("images") MultipartFile[] images) {
        try {
            String imagesPath = System.getProperty("images.path");

            String newLandmarkDirectoryName = "";
            if (name.contains("\"")) {
                newLandmarkDirectoryName = _imageService.concatenate(name.toLowerCase().replace(" ", "-").replace("\"", ""), "_", place.toString().toLowerCase());
            }
            else {
                newLandmarkDirectoryName = _imageService.concatenate(name.toLowerCase().replace(" ", "-"), "_", place.toString().toLowerCase());
            }
            File newLandmarkDirectory = new File(imagesPath, newLandmarkDirectoryName);
            
            List<FileDB> landmarkImageFiles = new ArrayList<>(); 
            
            if (newLandmarkDirectory.mkdir()) {
                        for (MultipartFile image : images) {
                            String imageName = image.getOriginalFilename();
                            String imageUrl = newLandmarkDirectoryName + "/" + imageName;
                            FileDB fileDB = new FileDB(imageName, image.getContentType(), imageUrl);
                            landmarkImageFiles.add(fileDB);                     
                            this._imageService.uploadImage(image, imageName, newLandmarkDirectory);    
                        }     
                    }
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            if (type.toString().toLowerCase().equals("historical")) {
                naturalType = null;
            }
            else {
                historicalRegion = null;
            }
            Landmark landmark = new Landmark(name, place, type, historicalRegion, naturalType, description, rating, dateFormat.format(Calendar.getInstance().getTime()) , 0, 0, 0, landmarkImageFiles.size(), landmarkImageFiles);

            _landmarkService.create(landmark);                
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return redirectView("http://localhost:8080/admin");
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RedirectView edit(@PathVariable String id, String name, String description, Integer rating, @RequestParam("images") MultipartFile[] images){
        Landmark landmark = _landmarkService.GetById(id);
        //boolean isDirectoryNameChanged = false;
        if (landmark != null) {
            String imagesPath = System.getProperty("images.path");
            String[] landmarkDirectoryNameParts = landmark.images.get(0).imagePath.split("/");
            String landmarkDirectoryName = landmarkDirectoryNameParts[0];
            File landmarkDirectory = new File(imagesPath, landmarkDirectoryName);

            List<FileDB> landmarkImageFiles = new ArrayList<>(); 

            if (!landmark.name.equals(name)) {
                File oldLandmarkDirectory = landmarkDirectory;

                String newLandmarkDirectoryName = _imageService.concatenate(name.toLowerCase().replace(" ", "-"), "_", landmark.place.toString().toLowerCase());
                File newLandmarkDirectory = new File(imagesPath, newLandmarkDirectoryName);

                if (newLandmarkDirectory.mkdir()) {
                    if (images[0].isEmpty()) {
                        try {
                            File source = new File(oldLandmarkDirectory.getAbsolutePath());
                            File dest = new File(newLandmarkDirectory.getAbsolutePath());
                            FileUploadUtils.copyDirectory(source, dest);
                            for (FileDB image : landmark.images) {
                                String imageUrl = newLandmarkDirectoryName + "/" + image.name;
                                image.imagePath = imageUrl;
                                landmarkImageFiles.add(image);
                                /*File imgFile = new File(imagesPath + image.imagePath);
                                byte[] imgBytes = Files.readAllBytes(imgFile.toPath());
                                CustomMultipartFile customMultipartFile = new CustomMultipartFile(imgBytes);
                                
                                customMultipartFile.transferTo();
                                landmarkImageFiles.add(image);                     
                                this._imageService.uploadImage(image, image.name, newLandmarkDirectory);    */
                            }
                            landmark.images = landmarkImageFiles;
                            landmark.numberOfImages = landmarkImageFiles.size();
                        }
                        catch(Exception exception) {
                            exception.printStackTrace();
                        }
                        _landmarkService.deleteDirectory(oldLandmarkDirectory);
                    }
                    else {
                        for (MultipartFile image : images) {
                            String imageName = image.getOriginalFilename();
                            String imageUrl = newLandmarkDirectoryName + "/" + imageName;
                            FileDB fileDB = new FileDB(imageName, image.getContentType(), imageUrl);
                            landmarkImageFiles.add(fileDB);                     
                            this._imageService.uploadImage(image, imageName, newLandmarkDirectory);    
                        }
                        landmark.images = landmarkImageFiles;
                        landmark.numberOfImages = landmarkImageFiles.size();
                    }
                }
            }
            else {
                if (landmarkDirectory.exists() && !images[0].isEmpty()) {
                    if (_landmarkService.deleteFileFromDirectory(landmarkDirectory)) {
                        for (MultipartFile image : images) {
                            String imageName = image.getOriginalFilename();
                            String imageUrl = landmarkDirectoryName + "/" + imageName;
                            FileDB fileDB = new FileDB(imageName, image.getContentType(), imageUrl);
                            landmarkImageFiles.add(fileDB);                     
                            this._imageService.uploadImage(image, imageName, landmarkDirectory);    
                        } 
                    }
                    landmark.images = landmarkImageFiles;
                    landmark.numberOfImages = landmarkImageFiles.size();
                }
            }

            landmark.name = name;
            landmark.description = description;
            landmark.rating = rating;
            _landmarkRepository.save(landmark);
        }

        return redirectView("http://localhost:8080/admin");
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RedirectView delete(@PathVariable String id){
        Landmark landmark = _landmarkService.GetById(id);
        if (landmark != null) {
            String imagesPath = System.getProperty("images.path");
            String landmarkDirectoryName = landmark.images.get(0).imagePath.split("/")[0];
            //String shoeDirectoryName = _shoeService.concatenate(shoe.brand.toString().toLowerCase(), "_", shoe.model.toLowerCase().replace(" ", "-"), "_", shoe.colorSpecification.toLowerCase().replace(" ", "-"));
            File landmarkDir = new File(imagesPath, landmarkDirectoryName);
            if (landmarkDir.exists()) {

                if (_landmarkService.deleteDirectory(landmarkDir)) {
                    _landmarkService.Delete(id);                 
                }
            }
        }

        return redirectView("http://localhost:8080/admin");
    }

    public RedirectView redirectView(String url){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }

}
