/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.transform.asm.instruction;

import com.google.common.base.Preconditions;
import net.flintmc.transform.asm.AbstractContext;
import net.flintmc.transform.asm.MethodVisitorContext;

@FunctionalInterface
public interface VisitMethodInsn {

  void visitMethodInsn(Context context);

  class Context implements AbstractContext {
    private MethodVisitorContext methodVisitorContext;
    private int opcode;
    private String owner;
    private String name;
    private String desc;
    private boolean itf;

    private Context(
        MethodVisitorContext methodVisitorContext,
        int opcode,
        String owner,
        String name,
        String desc,
        boolean itf) {
      this.methodVisitorContext = methodVisitorContext;
      this.opcode = opcode;
      this.owner = owner;
      this.name = name;
      this.desc = desc;
      this.itf = itf;
    }

    public static Context of(
        MethodVisitorContext methodVisitorContext,
        int opcode,
        String owner,
        String name,
        String desc,
        boolean itf) {
      Preconditions.checkNotNull(methodVisitorContext);
      Preconditions.checkNotNull(owner);
      Preconditions.checkNotNull(name);
      Preconditions.checkNotNull(desc);
      return new Context(methodVisitorContext, opcode, owner, name, desc, itf);
    }

    public int getOpcode() {
      return opcode;
    }

    public Context setOpcode(int opcode) {
      this.opcode = opcode;
      return this;
    }

    public String getOwner() {
      return owner;
    }

    public Context setOwner(String owner) {
      this.owner = owner;
      return this;
    }

    public MethodVisitorContext getMethodVisitorContext() {
      return methodVisitorContext;
    }

    public Context setMethodVisitorContext(MethodVisitorContext methodVisitorContext) {
      this.methodVisitorContext = methodVisitorContext;
      return this;
    }

    public String getName() {
      return name;
    }

    public Context setName(String name) {
      this.name = name;
      return this;
    }

    public String getDesc() {
      return desc;
    }

    public Context setDesc(String desc) {
      this.desc = desc;
      return this;
    }

    public boolean isItf() {
      return itf;
    }

    public Context setItf(boolean itf) {
      this.itf = itf;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitMethodInsn(
          this.opcode, this.owner, this.name, this.desc, this.itf);
      return this;
    }
  }
}
