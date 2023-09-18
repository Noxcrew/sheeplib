package com.noxcrew.sheeplib.widget.progress

import org.jetbrains.annotations.ApiStatus

/** The state of an ongoing operation. */
@ApiStatus.Experimental
public sealed interface Progress {

    /** The operation has not yet started. */
    public data object Waiting : Progress

    /** The operation is in progress. */
    public sealed interface InProgress : Progress {
        public val message: String

        /** The operation is in progress, and has completed [done] out of [total] operations. */
        public data class WithCount(override val message: String, public val done: Int, public val total: Int) :
            InProgress

        /** The operation is in progress, with no information about how much of the operation is complete. */
        public data class Indefinite(override val message: String) : InProgress
    }

    /** The operation has completed successfully. */
    public data object Complete : Progress

    /** The operation has failed due to an exception. */
    public data class Failed(public val reason: Throwable) : Progress
}
