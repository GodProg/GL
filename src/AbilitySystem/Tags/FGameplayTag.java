/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package AbilitySystem.Tags;

/**
 * A Tag is an object with a friendly name.
 * Allow to other object know that something has
 * happened or its hopping by adding them unique tags
 */
public final class FGameplayTag {

    /**
     * Tag's name
     */
    private final String TagName;

    private FGameplayTag() {
        TagName = null;
    }

    public FGameplayTag(String tagName) {
        TagName = tagName;
    }

    public String getTagName() {
        return TagName;
    }

    public boolean IsValid() {
        return TagName != null;
    }

    @Override
    public String toString() {
        return "FGameplayTag{" +
                "TagName='" + TagName + '\'' +
                '}';
    }
}
