/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package AbilitySystem.Tags;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stores A list of unique tags
 */
public class UTagContainer {
    /**
     * Tag container
     *
     * @see FGameplayTag
     */
    private HashMap<String, FGameplayTag> TagContainer;

    /**
     * Default constructor
     */
    public UTagContainer() {
        TagContainer = new HashMap<>();
    }

    /**
     * Checks if any tag exists within this container
     *
     * @param Tags the list of tags to check
     * @return true if at least one tag of the list is present on this container
     */
    public boolean HasAnyTag(ArrayList<FGameplayTag> Tags) {
        boolean Hasvalue = false;
        if (Tags != null) {
            for (FGameplayTag tag : Tags) {
                if (TagContainer.containsValue(tag)) {
                    Hasvalue = true;
                    break;
                }

            }
        }
        return Hasvalue;
    }

    /**
     * Checks if the given tag exists
     *
     * @param TagName the tag name
     * @return true if the tag exists
     */
    public boolean Hastag(String TagName) {
        return TagContainer.containsKey(TagName);
    }

    /**
     * Attempt to get a tag by name
     *
     * @param TagName the tag name
     * @return the tag if it exists else returns null
     */
    public FGameplayTag GetTag(String TagName) {
        //returns null if it does not exist
        return TagContainer.get(TagName);
    }

    /**
     * Adds a tag to the container
     *
     * @param tag The new Tag
     * @return true if the tag was successfully added. Returns false if the tag was already present.
     */
    public boolean Addtag(FGameplayTag tag) {
        return TagContainer.putIfAbsent(tag.getTagName(), tag) == null;
    }

    /**
     * Adds a list of tags to this container
     *
     * @param tags the list of tags to be added
     * @return true if any tag was added
     */
    public boolean AddAllTags(ArrayList<FGameplayTag> tags) {
        boolean Added = false;
        for (FGameplayTag tag : tags) {
            TagContainer.putIfAbsent(tag.getTagName(), tag);
            Added = true;
        }
        return Added;
    }

    /**
     * Same concept as AddAllTags
     *
     * @param tags the tags to be added
     * @return true if any tag was added
     */
    public boolean AddAllString(ArrayList<String> tags) {
        boolean Added = false;
        for (String tag : tags) {
            TagContainer.putIfAbsent(tag, new FGameplayTag(tag));
            Added = true;
        }
        return Added;
    }

    public ArrayList<FGameplayTag> GetAllTags() {
        return new ArrayList<>(TagContainer.values());
    }

    public void ClearContainer() {
        TagContainer.clear();
    }

    /**
     * Removes a tag from the container
     *
     * @param Tagname the tag to be removed
     * @return true if the tag was removed
     */
    public boolean RemoveTag(String Tagname) {
        return TagContainer.remove(Tagname) != null;
    }

    /**
     * Removes All tags from a list
     *
     * @param tags the list of tags to be removed
     * @return if the tags were removed
     */
    public boolean RemoveAllTags(ArrayList<FGameplayTag> tags) {
        return TagContainer.values().removeAll(tags);
    }

    /**
     * Removes All tags from a list
     *
     * @param tags the list of tags to be removed
     * @return if the tags were removed
     */
    public boolean RemoveAllTagsString(ArrayList<String> tags) {
        return TagContainer.keySet().removeAll(tags);
    }

    @Override
    public String toString() {
        return "UTagContainer{" +
                "TagContainer=" + TagContainer +
                '}';
    }
}
