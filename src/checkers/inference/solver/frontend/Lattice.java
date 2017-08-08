package checkers.inference.solver.frontend;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;

/**
 * Lattice class contains necessary information about qualifier hierarchy for
 * constraint constraint solving.
 * 
 * It is convenient to get all subtypes and supertypes of a specific type
 * qualifier, all type qualifier, and bottom and top qualifiers from an instance
 * of this class. In some back ends, for example, Max-SAT back end, a
 * relationship between a type qualifier and an integer number is used in
 * serialization stage. See
 * {@link checkers.inference.solver.backend.maxsatbackend.MaxSatSerializer}}
 * 
 * @author jianchu
 *
 */
public class Lattice {

    public final Map<AnnotationMirror, Collection<AnnotationMirror>> subType;
    public final Map<AnnotationMirror, Collection<AnnotationMirror>> superType;
    public final Map<AnnotationMirror, Collection<AnnotationMirror>> incomparableType;
    public final Map<AnnotationMirror, Integer> typeToInt;
    public final Map<Integer, AnnotationMirror> intToType;
    public final Set<? extends AnnotationMirror> allTypes;
    public final AnnotationMirror top;
    public final AnnotationMirror bottom;
    public final int numTypes;

    public Lattice(Map<AnnotationMirror, Collection<AnnotationMirror>> subType,
            Map<AnnotationMirror, Collection<AnnotationMirror>> superType,
            Map<AnnotationMirror, Collection<AnnotationMirror>> incomparableType,
            Map<AnnotationMirror, Integer> typeToInt, Map<Integer, AnnotationMirror> intToType,
            Set<? extends AnnotationMirror> allTypes, AnnotationMirror top, AnnotationMirror bottom,
            int numTypes) {
        this.subType = subType;
        this.superType = superType;
        this.incomparableType = incomparableType;
        this.typeToInt = typeToInt;
        this.intToType = intToType;
        this.allTypes = allTypes;
        this.top = top;
        this.bottom = bottom;
        this.numTypes = numTypes;
    }
}
