package checkers.inference.model;

import checkers.inference.util.ConstraintVerifier;
import org.checkerframework.framework.source.Result;
import org.checkerframework.framework.source.SourceChecker;
import org.checkerframework.framework.type.QualifierHierarchy;
import org.checkerframework.framework.type.VisitorState;
import org.checkerframework.javacutil.AnnotationUtils;
import org.checkerframework.javacutil.ErrorReporter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;

import checkers.inference.InferenceAnnotatedTypeFactory;
import checkers.inference.VariableAnnotator;

/**
 * Constraint manager holds constraints that are generated by InferenceVisitor.
 *
 * @author mcarthur
 *
 */
public class ConstraintManager {

    private boolean ignoreConstraints = false;

    private final Set<Constraint> constraints = new HashSet<Constraint>();

    private InferenceAnnotatedTypeFactory inferenceTypeFactory;

    private SourceChecker checker;

    private QualifierHierarchy realQualHierarchy;

    private VisitorState visitorState;

    private ConstraintVerifier constraintVerifier;

    public void init(InferenceAnnotatedTypeFactory inferenceTypeFactory) {
        this.inferenceTypeFactory = inferenceTypeFactory;
        this.realQualHierarchy = inferenceTypeFactory.getRealQualifierHierarchy();
        this.visitorState = inferenceTypeFactory.getVisitorState();
        this.checker = inferenceTypeFactory.getContext().getChecker();
        this.constraintVerifier = new ConstraintVerifier(realQualHierarchy);
    }

    public Set<Constraint> getConstraints() {
        return constraints;
    }

    public ConstraintVerifier getConstraintVerifier() {
        return constraintVerifier;
    }

    private void add(Constraint constraint) {
        if (!ignoreConstraints) {
            constraints.add(constraint);
        }
    }

    public void startIgnoringConstraints() {
        this.ignoreConstraints = true;
    }

    public void stopIgnoringConstraints() {
        this.ignoreConstraints = false;
    }

    public SubtypeConstraint createSubtypeConstraint(Slot subtype, Slot supertype) {
        if (subtype == null || supertype == null) {
            ErrorReporter.errorAbort("Create subtype constraint with null argument. Subtype: " + subtype
                    + " Supertype: " + supertype);
        }
        if (subtype instanceof ConstantSlot && supertype instanceof ConstantSlot) {
            ConstantSlot subConstant = (ConstantSlot) subtype;
            ConstantSlot superConstant = (ConstantSlot) supertype;

            if (!realQualHierarchy.isSubtype(subConstant.getValue(), superConstant.getValue())) {
                checker.report(Result.failure("subtype.type.incompatible", subtype, supertype),
                        visitorState.getPath().getLeaf());
            }
        }
        return new SubtypeConstraint(subtype, supertype, getCurrentLocation());
    }

    public EqualityConstraint createEqualityConstraint(Slot first, Slot second) {
        if (first == null || second == null) {
            ErrorReporter.errorAbort("Create equality constraint with null argument. Subtype: " + first
                    + " Supertype: " + second);
        }
        if (first instanceof ConstantSlot && second instanceof ConstantSlot) {
            ConstantSlot firstConstant = (ConstantSlot) first;
            ConstantSlot secondConstant = (ConstantSlot) second;
            if (!areSameType(firstConstant.getValue(), secondConstant.getValue())) {
                checker.report(Result.failure("equality.type.incompatible", first, second), visitorState
                        .getPath().getLeaf());
            }
        }
        return new EqualityConstraint(first, second, getCurrentLocation());
    }

    public InequalityConstraint createInequalityConstraint(Slot first, Slot second) {
        if (first == null || second == null) {
            ErrorReporter.errorAbort("Create inequality constraint with null argument. Subtype: "
                    + first + " Supertype: " + second);
        }
        if (first instanceof ConstantSlot && second instanceof ConstantSlot) {
            ConstantSlot firstConstant = (ConstantSlot) first;
            ConstantSlot secondConstant = (ConstantSlot) second;
            if (areSameType(firstConstant.getValue(), secondConstant.getValue())) {
                checker.report(Result.failure("inequality.type.incompatible", first, second),
                        visitorState.getPath().getLeaf());
            }
        }
        return new InequalityConstraint(first, second, getCurrentLocation());
    }

    public ComparableConstraint createComparableConstraint(Slot first, Slot second) {
        if (first == null || second == null) {
            ErrorReporter.errorAbort("Create comparable constraint with null argument. Subtype: "
                    + first + " Supertype: " + second);
        }
        if (first instanceof ConstantSlot && second instanceof ConstantSlot) {
            ConstantSlot firstConstant = (ConstantSlot) first;
            ConstantSlot secondConstant = (ConstantSlot) second;
            if (!realQualHierarchy.isSubtype(firstConstant.getValue(), secondConstant.getValue())
                    && !realQualHierarchy.isSubtype(secondConstant.getValue(), firstConstant.getValue())) {
                checker.report(Result.failure("comparable.type.incompatible", first, second),
                        visitorState.getPath().getLeaf());
            }
        }
        return new ComparableConstraint(first, second, getCurrentLocation());
    }

    public CombineConstraint createCombineConstraint(Slot target, Slot decl, Slot result) {
        if (target == null || decl == null || result == null) {
            ErrorReporter.errorAbort("Create combine constraint with null argument. Target: " + target
                    + " Decl: " + decl + " Result: " + result);
        }
        return new CombineConstraint(target, decl, result, getCurrentLocation());
    }

    public PreferenceConstraint createPreferenceConstraint(VariableSlot variable, ConstantSlot goal,
            int weight) {
        if (variable == null || goal == null) {
            ErrorReporter.errorAbort("Create preference constraint with null argument. Variable: "
                    + variable + " Goal: " + goal);
        }
        return new PreferenceConstraint(variable, goal, weight, getCurrentLocation());
    }

    public ExistentialConstraint createExistentialConstraint(Slot slot,
            List<Constraint> ifExistsConstraints, List<Constraint> ifNotExistsConstraints) {
        // TODO: add null checking for argument.
        return new ExistentialConstraint((VariableSlot) slot,
                ifExistsConstraints, ifNotExistsConstraints, getCurrentLocation());
    }

    private AnnotationLocation getCurrentLocation() {
        if (visitorState.getPath() != null) {
            return VariableAnnotator.treeToLocation(inferenceTypeFactory, visitorState.getPath()
                    .getLeaf());
        } else {
            return AnnotationLocation.MISSING_LOCATION;
        }
    }

    public void addSubtypeConstraint(Slot subtype, Slot supertype) {
        if ((subtype instanceof ConstantSlot)
                && this.realQualHierarchy.getTopAnnotations().contains(((ConstantSlot) subtype).getValue())) {
            this.addEqualityConstraint(supertype, (ConstantSlot) subtype);
        } else if ((supertype instanceof ConstantSlot)
                && this.realQualHierarchy.getBottomAnnotations().contains(
                        ((ConstantSlot) supertype).getValue())) {
            this.addEqualityConstraint(subtype, (ConstantSlot) supertype);
        } else {
            this.add(this.createSubtypeConstraint(subtype, supertype));
        }
    }

    public void addEqualityConstraint(Slot first, Slot second) {
        this.add(this.createEqualityConstraint(first, second));
    }

    public void addInequalityConstraint(Slot first, Slot second) {
        this.add(this.createInequalityConstraint(first, second));
    }

    public void addComparableConstraint(Slot first, Slot second) {
        this.add(this.createComparableConstraint(first, second));
    }

    public void addCombineConstraint(Slot target, Slot decl, Slot result) {
        this.add(this.createCombineConstraint(target, decl, result));
    }

    public void addPreferenceConstraint(VariableSlot variable, ConstantSlot goal, int weight) {
        this.add(this.createPreferenceConstraint(variable, goal, weight));
    }

    public void addExistentialConstraint(Slot slot, List<Constraint> ifExistsConstraints,
            List<Constraint> ifNotExistsConstraints) {
        this.add(this.createExistentialConstraint(slot, ifExistsConstraints, ifNotExistsConstraints));
    }

    private boolean areSameType(AnnotationMirror m1, AnnotationMirror m2) {
        return AnnotationUtils.areSameIgnoringValues(m1, m2);
    }
}
