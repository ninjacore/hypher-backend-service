package io.hypher.backendservice.platformdata.dto;

public class GildedProfilePage {
    
    MainContentBox mainContentBox;
    LinkCollectionContentBox linkCollectionContentBox;
    FeaturedContentBox featuredContentBox;

    public MainContentBox getMainContentBox() {
        return mainContentBox;
    }
    
    public void setMainContentBox(MainContentBox mainContentBox) {
        this.mainContentBox = mainContentBox;
    }
    
    public LinkCollectionContentBox getLinkCollectionContentBox() {
        return linkCollectionContentBox;
    }
    
    public void setLinkCollectionContentBox(LinkCollectionContentBox linkCollectionContentBox) {
        this.linkCollectionContentBox = linkCollectionContentBox;
    }
    
    public FeaturedContentBox getFeaturedContentBox() {
        return featuredContentBox;
    }
    
    public void setFeaturedContentBox(FeaturedContentBox featuredContentBox) {
        this.featuredContentBox = featuredContentBox;
    }

    public GildedProfilePage(MainContentBox mainContentBox, LinkCollectionContentBox linkCollectionContentBox,
            FeaturedContentBox featuredContentBox) {
        this.mainContentBox = mainContentBox;
        this.linkCollectionContentBox = linkCollectionContentBox;
        this.featuredContentBox = featuredContentBox;
    }

}
