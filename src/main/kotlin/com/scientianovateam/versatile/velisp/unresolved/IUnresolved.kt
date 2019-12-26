package com.scientianovateam.versatile.velisp.unresolved

import com.scientianovateam.versatile.velisp.evaluated.IEvaluated
import com.scientianovateam.versatile.velisp.unevaluated.IUnevaluated

interface IUnresolved {
    fun resolve(map: Map<String, IEvaluated>): IUnevaluated
    fun tryToResolve(map: Map<String, IEvaluated>): IUnresolved = this
}