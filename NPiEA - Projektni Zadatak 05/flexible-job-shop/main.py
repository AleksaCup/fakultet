import parser
import encoding
import random
#random.seed(1337)
import gantt
import matplotlib.pyplot as plt
import config
from genetic import GeneticAlgorithm

if __name__ == '__main__':
    job_number, machine_number, job_list = parser.job_parser('data/Brandimarte_Data/Text/Mk01.fjs')

    ga = GeneticAlgorithm(
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
    best_solution, fitnesess = ga.run(plot=True)
    plt.plot(fitnesess)
    plt.xlabel("Generation")
    plt.ylabel("Average fitness")
    plt.show()
    print("=========================================")

    best_solution = best_solution[0]
    decoded = encoding.genes_to_solution(best_solution, job_list, machine_number)
    for x in decoded:
        print(x)
    print("=========================================")
    decoded_to_gantt = encoding.translate_decoded_to_gantt(decoded)
    for key, value in decoded_to_gantt.items():
        print(f"{key}: {value}")
    print("=========================================")
    gantt.draw_chart(decoded_to_gantt)