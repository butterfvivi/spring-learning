package org.vivi.framework.dynamic.sqlbatis2.ognl;

import ognl.MemberAccess;
import org.vivi.framework.dynamic.sqlbatis2.reflection.Reflector;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.Map;

public class OgnlMemberAccess implements MemberAccess {
    private final boolean canControlMemberAccessible;

    public OgnlMemberAccess() {
        this.canControlMemberAccessible = Reflector.canControlMemberAccessible();
    }

    @Override
    public Object setup(Map context, Object target, Member member, String propertyName) {
        Object result = null;
        if (isAccessible(context, target, member, propertyName)) {
            AccessibleObject accessible = (AccessibleObject) member;
            if (!accessible.isAccessible()) {
                result = Boolean.FALSE;
                accessible.setAccessible(true);
            }
        }
        return result;
    }

    @Override
    public void restore(Map context, Object target, Member member, String propertyName, Object state) {
        if (state != null) {
            ((AccessibleObject) member).setAccessible((Boolean) state);
        }
    }

    @Override
    public boolean isAccessible(Map context, Object target, Member member, String propertyName) {
        return canControlMemberAccessible;
    }
}
