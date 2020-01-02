package idea.plugin

import arrow.meta.MetaPlugin
import arrow.meta.Plugin
import arrow.meta.ide.IdeMetaPlugin
import arrow.meta.ide.dsl.IdeSyntax
import arrow.meta.phases.CompilerContext
import kotlin.contracts.ExperimentalContracts


open class IdeMetaPlugin : MetaPlugin(), IdeSyntax {
    @ExperimentalContracts
    override fun intercept(ctx: CompilerContext): List<Plugin> =
            super.intercept(ctx)// + purity
}
