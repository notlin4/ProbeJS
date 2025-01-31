package moe.wolfgirl.probejs.lang.typescript.code.type.js;

import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.ImportInfo;
import moe.wolfgirl.probejs.lang.typescript.code.member.MethodDecl;
import moe.wolfgirl.probejs.lang.typescript.code.member.ParamDecl;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;

import java.util.*;

public class JSLambdaType extends BaseType {
    public final List<ParamDecl> params;
    public final BaseType returnType;

    public JSLambdaType(List<ParamDecl> params, BaseType returnType) {
        this.params = params;
        this.returnType = returnType;
    }

    @Override
    public Collection<ImportInfo> getUsedImports() {
        Set<ImportInfo> classPaths = new HashSet<>(returnType.getUsedImports());
        for (ParamDecl param : params) {
            classPaths.addAll(param.type.getUsedImports());
        }
        return classPaths;
    }

    // TODO: type-aware formatting instead of hardcoding at builder level
    @Override
    public List<String> format(Declaration declaration, FormatType input) {
        // (arg0: type, arg1: type...) => returnType
        return List.of("%s => %s".formatted(
                ParamDecl.formatParams(params, declaration, input == FormatType.RETURN ? FormatType.INPUT : FormatType.RETURN),
                returnType.line(declaration, input))
        );
    }

    public MethodDecl asMethod(String methodName) {
        return new MethodDecl(methodName, List.of(), params, returnType);
    }

    public static class Builder {
        public final List<ParamDecl> params = new ArrayList<>();
        public BaseType returnType = Types.VOID;
        public boolean arrowFunction = true;

        public Builder returnType(BaseType type) {
            this.returnType = Types.ignoreContext(type, arrowFunction ? FormatType.INPUT : FormatType.RETURN);
            return this;
        }

        public Builder param(String symbol, BaseType type) {
            return param(symbol, type, false);
        }

        public Builder param(String symbol, BaseType type, boolean isOptional) {
            return param(symbol, type, isOptional, false);
        }

        public Builder param(String symbol, BaseType type, boolean isOptional, boolean isVarArg) {
            params.add(new ParamDecl(symbol, Types.ignoreContext(type, arrowFunction ? FormatType.RETURN : FormatType.INPUT), isVarArg, isOptional));
            return this;
        }

        public Builder method() {
            arrowFunction = false;
            return this;
        }

        public JSLambdaType build() {
            return new JSLambdaType(params, returnType);
        }
    }
}
