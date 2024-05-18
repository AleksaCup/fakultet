import config
import encoding
from typing import List
import random

import gantt
import parser
import matplotlib.pyplot as plt
random.seed(1337) #For easy debuging

class GeneticAlgorithm:
    def __init__(self, population_size, number_of_generations, crossover_rate, mutation_rate, jobs_data, job_number, machine_number, termination_variancy, termination_variancy_len=15, mutaation_rate_channge=0.6, mutation_rate_change_interval=50, elitisam_rate=0.3,elitism_rate_change=0.05, elitism_rate_max=0.3, elitism_rate_increase_interval=10, min_gen_number=100):
        # Initialize GA parameters and data about jobs and machines
        self.population_size = population_size
        self.number_of_generations = number_of_generations
        self.crossover_rate = crossover_rate
        self.mutation_rate = mutation_rate
        self.jobs_data = jobs_data
        self.job_number = job_number
        self.machine_number = machine_number
        self.mutaation_rate_channge = mutaation_rate_channge
        self.mutation_rate_change_interval = mutation_rate_change_interval
        self.elitisam_rate = elitisam_rate
        self.elitism_rate_change = elitism_rate_change
        self.elitism_rate_max = elitism_rate_max
        self.elitism_rate_increase_interval = elitism_rate_increase_interval
        self.terminate_variancy = termination_variancy
        self.terminate_variancy_len = termination_variancy_len
        self.min_gen_number = min_gen_number

    def initialize_population(self):
        # Generate initial population of feasible schedules

        self.population = encoding.initialize_population(self.jobs_data, self.population_size)

    def calculate_fitness(self, individual):
        # Calculate fitness of an individual based on total completion time and constraints
        solution=encoding.genes_to_solution(individual,self.jobs_data,self.machine_number)
        return encoding.get_time_span(solution)

    def selection(self, population:List[dict]):
        # Select individuals for reproduction
        population=population.copy()
        population.sort(key=lambda x: self.calculate_fitness(x))
        return population[:int(len(population))]

    def crossover(self, parent1, parent2):
        # Combine two individuals to create a new one
        child1 = {"ms":[],"os":[]}
        child2 = {"ms":[],"os":[]}

        #For each individual take a random gene from one of the parents
        #Other one is taken from the other parent
        for g1,g2 in zip(parent1["ms"],parent2["ms"]):
            g=[g1,g2]
            random.shuffle(g)
            child1["ms"].append(g[0])
            child2["ms"].append(g[1])

        #Since the os is a permutation, we take a random permutation from one of the parents
        #Other one is taken from the other parent
        os_g=[parent1["os"],parent2["os"]]
        random.shuffle(os_g)
        child1["os"]=os_g[0]
        child2["os"]=os_g[1]

        return child1,child2

    def mutate(self, individual):
        # Randomly alter an individual
        ms=individual["ms"]
        os=individual["os"]

        i=0
        for job in self.jobs_data:
            for op in job:
                if  random.random() < self.mutation_rate:
                    #If we mutate, we take a new random machine number
                    ms[i]=(random.randint(0, len(op) - 1))
                i+=1

        if random.random() < self.mutation_rate:
            #If we mutate, we take a random number of permutations
            number_of_permutations=random.randint(1, len(os)//2)
            for i in range(number_of_permutations):
                #We take two random indices and swap them
                a=random.randint(0, len(os) - 1)
                b=random.randint(0, len(os) - 1)
                os[a],os[b]=os[b],os[a]

        individual["ms"]=ms
        individual["os"]=os
        return individual

    def run(self,plot=False):
        """
        Main function of the algorithm
        :return:
        """
        # This will be used for termination condition
        previous_fitness=[]

        # This will be used for plotting
        fitnesses=[]

        # Initialize population
        self.initialize_population()

        # Loop for number of generations
        for generation_number in range(self.number_of_generations):

            # As the algorithm progresses, we want to decrease the mutation rate
            if generation_number%self.mutation_rate_change_interval==0:
                self.mutation_rate=self.mutation_rate*self.mutaation_rate_channge

            # As the algorithm progresses, we want to increase the elitism rate
            if generation_number%self.elitism_rate_increase_interval==0:
                config.elitism_rate+=self.elitism_rate_change
                config.elitism_rate=min(config.elitism_rate,self.elitism_rate_max)

            # Selection
            self.population=self.selection(self.population)

            # Elitism
            elites=self.population[:int(len(self.population)*config.elitism_rate)]

            # Crossover
            children_population=[]
            population_copy=self.population.copy()
            for i in range(0,len(self.population),2):
                if i == len(self.population)-1:
                    break
                child1,child2=self.crossover(self.population[i],self.population[i+1])
                children_population.append(child1)
                children_population.append(child2)

            children_population=children_population[:len(self.population)-len(elites)]
            for individual in children_population:
                self.mutate(individual)

            self.population=elites+children_population


            # Calculate fitness of the population
            max_fitness=min([self.calculate_fitness(x) for x in self.population])
            average_fitness=sum([self.calculate_fitness(x) for x in self.population])/len(self.population)
            #median_fitness=sorted([self.calculate_fitness(x) for x in self.population])[len(self.population)//2]
            #weighted_fitness=sum([self.calculate_fitness(x)*len(self.population)-i for i,x in enumerate(self.population)])/sum([len(self.population)-i for i,x in enumerate(self.population)])

            fitnesses.append(average_fitness)
            previous_fitness.append(average_fitness)

            if len(previous_fitness)>self.terminate_variancy_len:
                previous_fitness.pop(0)

            if len(previous_fitness)==self.terminate_variancy_len and generation_number>self.min_gen_number:
                #If the variance of the last terminate_variancy_len generations is less than the termination_variancy
                #we terminate the algorithm
                if max(previous_fitness)-min(previous_fitness)<self.terminate_variancy:
                    break

            if generation_number%50==0:
                print(f"Generation {generation_number}: {max_fitness}; Pop size: {len(self.population)}")

        self.population.sort(key=lambda x: self.calculate_fitness(x))
        if plot:
            return self.population,fitnesses
        return self.population




if __name__ == '__main__':
    job_number, machine_number, job_list = parser.job_parser('data/Brandimarte_Data/Text/Mk01.fjs')

    ga=GeneticAlgorithm(
        config.population_size,
        config.number_of_generations,
        config.crossover_rate,
        config.mutation_rate,
        job_list,
        job_number,
        machine_number,
        config.termination_variancy,
        config.termination_variancy_len,
        config.mutation_rate_change,
        config.mutation_rate_change_interval,
        config.elitism_rate,
        config.elitism_rate_change,
        config.elitism_rate_max,
        config.elitism_rate_increase_interval
    )
    best_solution,fitnesess=ga.run(plot=True)
    plt.plot(fitnesess)
    plt.xlabel("Generation")
    plt.ylabel("Average fitness")
    plt.show()
    print("=========================================")

    best_solution=best_solution[0]
    decoded=encoding.genes_to_solution(best_solution,job_list,machine_number)
    for x in decoded:
        print(x)
    print("=========================================")
    decoded_to_gantt=encoding.translate_decoded_to_gantt(decoded)
    for key,value in decoded_to_gantt.items():
        print(f"{key}: {value}")
    print("=========================================")
    gantt.draw_chart(decoded_to_gantt)