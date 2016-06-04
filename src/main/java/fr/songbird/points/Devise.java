package main.java.fr.songbird.points;

import main.java.fr.songbird.constants.ProgramConstants;
import main.java.fr.songbird.groovyresources.YamlFileSkeleton;

import java.util.Map;
import java.util.Set;

/**
 * Chargement les devises.<br>
 * A partir de cette classe et de ses accesseurs, vous pourrez obtenir le ratio points de bataille/points d'honneur.<br>
 * Par exemple, si la classe Devise charge le ratio: 1 pts de bataille vaut 3 pts d'honneur<br>
 * Alors 30 pts de bataille = 90 points d'honneur
 */
public class Devise implements ProgramConstants
{
    /**
     * Multiplicateur de points de bataille
     */
    private final int ptsCoeff;

    public Devise()
    {
        Object object = new YamlFileSkeleton(genericConfigFile).loadYamlFile();
        assert(object instanceof Map) :"object="+object;
        Map.Entry entries = (Map.Entry)((Map)object).entrySet();
        assert(entries.getKey() instanceof String && entries.getValue() instanceof Integer) : "Les données ne sont pas intègres.";
        Map<String, Integer> fileSkeleton =  (Map<String, Integer>)object;
        Object value = fileSkeleton.get("honorPts");
        assert(value != null): "La clé honorPts n'existe pas.";
        assert(value instanceof Integer): "La valeur n'est pas un entier.";
        ptsCoeff = (int)value;
    }

    /**
     *
     * @return honor points coefficient
     */
    public final int getCoeff()
    {
        return ptsCoeff;
    }
}
