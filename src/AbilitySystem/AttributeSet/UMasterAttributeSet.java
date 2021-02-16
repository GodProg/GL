/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package AbilitySystem.AttributeSet;

import AbilitySystem.Implementation.IAttributeSetCallable;
import AbilitySystem.Utils.FScalableFloat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds default attributes like Health/Armor Etc...
 */
public abstract class UMasterAttributeSet {
    /**
     * Callback object to invoke when an attribute has changed
     */
    private IAttributeSetCallable callable;

    /**
     * Holds all the attributes within this set
     */
    private HashMap<String, FScalableFloat> Attributes = new HashMap<>();

    /**
     * Initialize this attribute set
     */
    private UMasterAttributeSet() {
        MakeModifiers();

    }


    /**
     * Initialize this attribute set
     *
     * @param callable @see IAttributeSetCallable
     */
    public UMasterAttributeSet(IAttributeSetCallable callable) {
        this.callable = callable;
        MakeModifiers();

    }

    /**
     * Loads an attribute set from a file
     *
     * @param FilePath the attribute set path
     * @return The list of all attributes within the file
     */
    public static final ArrayList<FScalableFloat> LoadFromAttrFile(String FilePath) {
        File F = new File(FilePath);
        ArrayList<FScalableFloat> AttributesFound = new ArrayList<>();

        if (F.getName().endsWith(".attr")) {
            if (!F.exists()) {
                System.out.printf("Failed to load Attributes from [%s]. Doesn't exist!\n", FilePath);
            } else {

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(FilePath));
                    String line = reader.readLine();
                    while (line != null) {
                        try {
                            AttributesFound.add(new FScalableFloat(Float.parseFloat(line.split("->")[1]), line.split("->")[0].replace(" ", "")));
                        } catch (NumberFormatException e) {
                            System.out.println(e);
                        }
                        line = reader.readLine();
                    }
                    reader.close();
                } catch (Exception e) {
                    System.out.printf("failed to read attribute in file [%s]\nTrace: [%S]\n", FilePath, e.getMessage());
                }
            }
        } else {
            System.out.printf("Invalid File! Expected [.attr] but got [%s]\n", FilePath);
        }

        return AttributesFound;
    }

    /**
     * Add attributes dynamically after this object has been instantiated
     *
     * @param Attribute The attribute to be added
     */
    public final void AddDynamicAttribute(FScalableFloat Attribute) {
        Attributes.put(Attribute.getName(), Attribute);
    }

    /**
     * Function used to define the attributes on children classes
     */
    protected abstract void MakeModifiers();

    /**
     * Change an attribute within this set
     * <p>
     * If the attribute exists it will update the values with the new attribute
     *
     * @param NewAttribute the attribute to be changed
     */
    public final void ChangeAttribute(FScalableFloat NewAttribute) {
        for (Map.Entry<String, FScalableFloat> Set : Attributes.entrySet()) {
            if (NewAttribute.getName() == Set.getKey()) {
                if (callable != null) {
                    callable.OnAttributeChanged(NewAttribute, Set.getValue());
                }

                OnAttributeChanged(NewAttribute, Set.getValue());
                Attributes.put(NewAttribute.getName(), NewAttribute);
                break;

            }
        }
    }

    /**
     * Change an attribute within this set
     * <p>
     * If the attribute exists it will update the values with the new attribute
     *
     * @param NewAttribute the attribute to be changed
     * @param NotifyBack   Should callable be notified
     */
    public final void ChangeAttribute(FScalableFloat NewAttribute, boolean NotifyBack) {
        for (Map.Entry<String, FScalableFloat> Set : Attributes.entrySet()) {
            if (NewAttribute.getName().equals(Set.getKey())) {
                if (callable != null) {
                    if (NotifyBack) callable.OnAttributeChanged(NewAttribute, Set.getValue());
                }

                OnAttributeChanged(NewAttribute, Set.getValue());
                Attributes.put(NewAttribute.getName(), NewAttribute);
                break;

            }
        }
    }

    /**
     * Called when an attribute has changed
     *
     * @param NewAttribute the new attribute value
     * @param OldAttribute the attribute that was replaced
     */
    public abstract void OnAttributeChanged(FScalableFloat NewAttribute, FScalableFloat OldAttribute);


    /**
     * returns a copy of an attribute
     *
     * @param Attr The attribute to seek
     * @return A copy of the attribute
     */
    public final FScalableFloat GetAttribute(String Attr) {
        //return a copy not a reference
        return new FScalableFloat(Attributes.get(Attr));
    }

    public HashMap<String, FScalableFloat> GetAttributes() {
        return Attributes;
    }

    @Override
    public String toString() {

        String finalresult = "UMasterAttributeSet{\n";

        for (Map.Entry<String, FScalableFloat> attr : Attributes.entrySet()) {
            finalresult += String.format("\t%s->%s\n", attr.getKey(), attr.getValue());
        }
        finalresult += "}";

        return finalresult;

        /*return "UMasterAttributeSet{" +
                "Attributes=" + Attributes +
                '}';*/
    }
}
