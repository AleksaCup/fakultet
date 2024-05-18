import random

def jobs_to_genes(jobs):
    """
    @brief: This function encodes jobs to genes
    @details: This function makes a random gene from jobs.
    :param jobs: A list of jobs that consists of a list of tuples that represent operation choices for each operation
    :return: {
        "ms":machine_string,
        "os":operation_string
    }
    """
    machine_string=[]
    operation_string=[]

    i=0
    for job in jobs:
        for op in job:
            operation_string.append(i)
            machine_string.append(random.randint(0,len(op)-1))
        i=i+1
    random.shuffle(operation_string)

    return {
        "ms":machine_string,
        "os":operation_string
    }

def initialize_population(jobs, population_size):
    """
    @brief: This function initializes population
    @details: This function makes a random population from jobs.
    :param jobs: A list of jobs that consists of a list of tuples that represent operation choices for each operation
    :param population_size:int The size of population
    :return: A list of population
    """
    population=[]
    for i in range(population_size):
        population.append(jobs_to_genes(jobs))
    return population

def __is_free(tab,start,duration):
    for k in range(start,start+duration):
        if not tab[k]:
            return False
    return True

def __find_first_available_place(start_cstr, prcTime, param):

    max_duration_list = []
    max_duration = start_cstr + prcTime

    # max_duration is either the start_ctr + duration or the max(possible starts) + duration
    if param:
        for job in param:
            max_duration_list.append(job[3] + job[1])  # start + process time

        max_duration = max(max(max_duration_list), start_cstr) + prcTime

    machine_used = [True] * max_duration

    # Updating array with used places
    for job in param:
        start = job[3]
        long = job[1]
        for k in range(start, start + long):
            machine_used[k] = False

    # Find the first available place that meets constraint
    for k in range(start_cstr, len(machine_used)):
        if __is_free(machine_used, k, prcTime):
            return k

def genes_to_solution(genes, jobs, machine_number):
    """
    @brief: This function decodes genes to solution
    @details: This function decodes genes to solution that sort-of represents a gantt chart
    :param genes: A dictionary that consists of machine string and operation string
    :param jobs: A list of jobs that consists of a list of tuples that represent operation choices for each operation
    :param machine_number:int The number of machines
    :return: A list of gannt chart
    """
    machine_string=genes["ms"]
    operation_string=genes["os"]

    machine_operations = [[] for i in range(machine_number)]

    # Splitting machine string to get machies for each job
    machine_string_split =[]
    current_machine = 0
    for i, current_job in enumerate(jobs):
        machine_string_split.append(machine_string[current_machine:current_machine+len(current_job)])
        current_machine = current_machine+len(current_job)


    # Iterating over OS to get task execution order and then checking in
    indexes = [0] * len(machine_string_split)
    start_task_cstr = [0] * len(machine_string_split)

    for job in operation_string:
        # machine_string_split[job] is the list of machines for the current job
        # indexes[job] is the index of the current task for the current job
        index_machine = machine_string_split[job][indexes[job]] #index of the machine for the current task
        machine = jobs[job][indexes[job]][index_machine][0] #machine number
        prcTime = jobs[job][indexes[job]][index_machine][1] #process time
        start_cstr = start_task_cstr[job] #start constraint

        # Getting the first available place for the operation
        start = __find_first_available_place(start_cstr, prcTime, machine_operations[machine - 1])
        name_task = "{}-{}".format(job, indexes[job]+1) #name of the task; 0-4 means job 0, task 4

        machine_operations[machine - 1].append((name_task, prcTime, start_cstr, start)) #adding task to the machine

        # Updating indexes (one for the current task for each job, one for the start constraint
        # for each job)
        indexes[job] += 1
        start_task_cstr[job] = (start + prcTime)

    return machine_operations

#This is the optimum criteria
def get_time_span(solution):
    """
    @brief: This function gets time span of a solution
    @details: This function gets time span of a solution
    :param solution: A list of gannt chart
    :return: Time span
    """
    max_time = 0
    for machine in solution:
        for task in machine:
            if task[3] + task[1] > max_time:
                max_time = task[3] + task[1]
    return max_time

def translate_decoded_to_gantt(machine_operations):
    data = {}

    for idx, machine in enumerate(machine_operations):
        machine_name = f"Machine-{idx + 1}"
        operations = []
        for operation in machine:
            operations.append([operation[3], operation[3] + operation[1], operation[0]])

        data[machine_name] = operations

    return data