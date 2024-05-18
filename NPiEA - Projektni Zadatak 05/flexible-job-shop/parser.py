from queue import Queue

def job_parser(filename:str):
    """
    Parses a job file
    :param filename:Path to a job file to parse
    :return: job_number:int, machine_number:int, job_list:tuple
    """

    #Return variabels
    job_number=0
    machine_number=0
    job_list=[]

    with open(filename,'r') as f:
        #Read header
        header=tuple(f.readline().strip().split())
        header=[int(header[0]),int(header[1])]

        #Number of jobs for scheduling
        job_number=header[0]
        #Number of available machines
        machine_number=header[1]

        #Read jobs
        for _ in range(job_number):
            job=[]
            line=tuple(f.readline().strip().split())
            line=[int(x) for x in line]
            #Number of operations for the job
            operation_nummber=line[0]

            #Read operations
            opearations=Queue()
            opearations.queue.extend(line[1:])

            for _ in range(operation_nummber):
                operation_posibilities=[]
                #Number of machines that can do the operation
                macine_posibilites=opearations.get()
                for _ in range(macine_posibilites):
                    #Machine index
                    machine_index=opearations.get()
                    #Processing time
                    time_taken=opearations.get()
                    operation_posibilities.append((machine_index,time_taken))
                job.append(tuple(operation_posibilities))
            job_list.append(tuple(job))
    return job_number,machine_number,tuple(job_list)



if __name__=='__main__':
    job_number,machin_number,job_list=job_parser('./data/test.fjs')
    print(f"Job number: {job_number}; Machine number: {machin_number}")
    for job in job_list:
        print(job)
