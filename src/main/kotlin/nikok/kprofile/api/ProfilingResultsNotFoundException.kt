/**
 *@author Nikolaus Knop
 */

package nikok.kprofile.api

class ProfilingResultsNotFoundException internal constructor(topic: String, tags: List<Tag>)
    : Exception("Didn't find profiling results for topic $topic and tags $tags")