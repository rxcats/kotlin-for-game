package io.github.rxcats

import arrow.core.Either
import arrow.core.raise.result
import arrow.core.flatMap
import arrow.core.left
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import arrow.core.right
import org.junit.jupiter.api.Test

class JobsArrowTest {
    data class Job(val id: Long, val company: Company, val salary: Salary)
    data class Company(val name: String)
    data class Salary(val value: Double)

    class Jobs {
        private val pool: Map<Long, Job> = mapOf(
            1L to Job(1, Company("Apple"), Salary(100.0)),
            2L to Job(2, Company("Google"), Salary(101.0)),
            3L to Job(3, Company("Meta"), Salary(102.0)),
            4L to Job(4, Company("Microsoft"), Salary(103.0)),
            5L to Job(5, Company("Amazon"), Salary(104.0)),
        )

        fun findById(id: Long): Result<Job?> = runCatching { pool[id] }
        fun findAll(): Result<List<Job>> = Result.success(pool.values.toList())

        fun findByIdE(id: Long): Either<JobError, Job> = Either.catch { pool[id] }
            .mapLeft { GenericError(it.message) }
            .flatMap { maybeJob -> maybeJob?.right() ?: JobNotFoundError(id).left() }

        fun findAllE(): Either<JobError, List<Job>> = pool.values.toList().right()
    }

    private val jobs = Jobs()

    private fun List<Job>.maxSalary(): Result<Salary> = runCatching {
        if (this.isEmpty()) {
            throw NoSuchElementException("no jobs present")
        } else {
            this.maxBy { it.salary.value }.salary
        }
    }

    private fun List<Job>.maxSalaryE(): Either<JobError, Salary> =
        if (this.isEmpty()) {
            GenericError("no jobs present").left()
        } else {
            this.maxBy { it.salary.value }.salary.right()
        }

    private fun getSalaryGapVsMax(jobId: Long): Result<Double> = jobs.findById(jobId).flatMap { maybeJob ->
        val salary = maybeJob?.salary ?: Salary(0.0)
        jobs.findAll().flatMap { jobList ->
            jobList.maxSalary().map { maxSalary ->
                maxSalary.value - salary.value
            }
        }
    }

    private fun getSalaryGapVsMaxArrow(jobId: Long): Result<Double> = result {
        val maybeJob: Job? = jobs.findById(jobId).bind()
        ensureNotNull(maybeJob) { NoSuchElementException("job not found") }
        val jobSalary = maybeJob.salary
        val jobList = jobs.findAll().bind()
        val maxSalary = jobList.maxSalary().bind()
        maxSalary.value - jobSalary.value
    }


    private fun getSalaryGapVsMaxArrowE(jobId: Long): Either<JobError, Double> = either {
        val maybeJob: Job = jobs.findByIdE(jobId).bind()
        val jobSalary = maybeJob.salary
        val jobList = jobs.findAllE().bind()
        val maxSalary = jobList.maxSalaryE().bind()
        maxSalary.value - jobSalary.value
    }

    sealed interface JobError
    data class JobNotFoundError(val jobId: Long) : JobError
    data class GenericError(val message: String? = "Unknown error") : JobError

    @Test
    fun getSalaryGapVsMaxTest() {
        val r = getSalaryGapVsMax(1)

        r.fold({
            println("r: $it")
        }, {
            println("error: $it")
        })
    }

    @Test
    fun getSalaryGapVsMaxArrowTest() {
        val r = getSalaryGapVsMaxArrow(1)

        r.fold({
            println("r: $it")
        }, {
            println("error: $it")
        })
    }

    @Test
    fun getSalaryGapVsMaxArrowETest() {
        val r = getSalaryGapVsMaxArrowE(1)

        r.fold({
            println("error: $it")
        }, {
            println("r: $it")
        })
    }

}
