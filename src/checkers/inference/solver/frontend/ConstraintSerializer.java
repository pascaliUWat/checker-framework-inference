package checkers.inference.solver.frontend;

import java.lang.reflect.Constructor;

import checkers.inference.model.CombVariableSlot;
import checkers.inference.model.CombineConstraint;
import checkers.inference.model.ComparableConstraint;
import checkers.inference.model.ConstantSlot;
import checkers.inference.model.EqualityConstraint;
import checkers.inference.model.ExistentialConstraint;
import checkers.inference.model.ExistentialVariableSlot;
import checkers.inference.model.InequalityConstraint;
import checkers.inference.model.PreferenceConstraint;
import checkers.inference.model.RefinementVariableSlot;
import checkers.inference.model.Serializer;
import checkers.inference.model.SubtypeConstraint;
import checkers.inference.model.VariableSlot;
import checkers.inference.solver.backend.BackEndType;
import checkers.inference.solver.backend.BackEndType.BackEndTypeEnum;

/**
 * ConstraintSerializer is a deliverer that can delegate each type of
 * constraint/slot to the real serializer that is specified by user.
 * 
 * The costume serialization can be achieved by overriding the serialize methods
 * in this class.
 * 
 * @author jianchu
 *
 * @param <S> Encoding type for slot.
 * @param <T> Encoding type for constraint.
 */
public class ConstraintSerializer<S, T> implements Serializer<S, T> {

    private Serializer<S, T> realSerializer;

    @SuppressWarnings("unchecked")
    public ConstraintSerializer(BackEndType backEndType, Lattice lattice) {
        try {
            String backEndPath = backEndType.getFullyQualifiedName();
            if (backEndType.getBackEndType().equals(BackEndTypeEnum.Lingeling)) {
                // Lingeling back ends also uses Max-SAT serializer.
                backEndPath = backEndType.getFullyQualifiedName(BackEndType.BackEndTypeEnum.MaxSAT);
            }
            System.out.println("PATH!!!+" + backEndPath);
            Class<?> serializerClass = Class.forName(backEndPath + "Serializer");
            Constructor<?> cons = serializerClass.getConstructor(Lattice.class);
            realSerializer = (Serializer<S, T>) cons.newInstance(lattice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public T serialize(SubtypeConstraint constraint) {
        return realSerializer.serialize(constraint);
    }

    @Override
    public T serialize(EqualityConstraint constraint) {
        return realSerializer.serialize(constraint);
    }

    @Override
    public T serialize(ExistentialConstraint constraint) {
        return realSerializer.serialize(constraint);
    }

    @Override
    public T serialize(InequalityConstraint constraint) {
        return realSerializer.serialize(constraint);
    }

    @Override
    public S serialize(VariableSlot slot) {
        return realSerializer.serialize(slot);
    }

    @Override
    public S serialize(ConstantSlot slot) {
        return realSerializer.serialize(slot);
    }

    @Override
    public S serialize(ExistentialVariableSlot slot) {
        return realSerializer.serialize(slot);
    }

    @Override
    public S serialize(RefinementVariableSlot slot) {
        return realSerializer.serialize(slot);
    }

    @Override
    public S serialize(CombVariableSlot slot) {
        return realSerializer.serialize(slot);
    }

    @Override
    public T serialize(ComparableConstraint comparableConstraint) {
        return realSerializer.serialize(comparableConstraint);
    }

    @Override
    public T serialize(CombineConstraint combineConstraint) {
        return realSerializer.serialize(combineConstraint);
    }

    @Override
    public T serialize(PreferenceConstraint preferenceConstraint) {
        return realSerializer.serialize(preferenceConstraint);
    }

}
