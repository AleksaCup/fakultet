from datetime import datetime
import networkx as nx
import pickle
import igraph as ig 

reacts_weight = {
    "special": 50,
    "loves": 25,
    "wows": 20,
    "sads": 15,
    "hahas": 15,
    "angrys": 15,
    "likes": 10
}


def time_decay_parameter(action_date):

    current_date = datetime.today()
    day_difference = (current_date - action_date).days
    base_multiplier = 50

    if day_difference <= 0:
        return base_multiplier

    multiplier = base_multiplier * (1 / (1 + 0.05 * day_difference))
    return multiplier if multiplier >= 0 else 0.0001


def calculate_affinity(logged_user_name, interacted_user_name, comments, reacts, shares, statuses) -> float:

    sum_affinity = 0
    comment_rank = 0
    comment_weight = 50

    #AFFINITY ZA KOMENTARE
    if comments.get(logged_user_name) is None:
        comment_rank = 0
    else:
        for comment in comments.get(logged_user_name):
            status = statuses.get(comment['status_id'])
            if status is not None and status['author'] == interacted_user_name:
                action_date = comment['comment_published']
                multiplier = time_decay_parameter(action_date)
                comment_rank += comment_weight * multiplier

    #AFFINITY ZA REAKCIJE
    reacts_rank = 0

    if reacts.get(logged_user_name) is None:
        reacts_rank = 0
    else:
        for react in reacts.get(logged_user_name):
            status = statuses.get(react['status_id'])

            if status is not None and status['author'] == interacted_user_name:
                action_date = react['reacted']
                multiplier = time_decay_parameter(action_date)
                reacts_rank += reacts_weight[react['type_of_reaction']] * multiplier

    #AFFINITY ZA SEROVE
    shares_rank = 0
    share_weight = 80

    if shares.get(logged_user_name) is None:
        shares_rank = 0
    else:
        for share in shares.get(logged_user_name):
            status = statuses.get(share['status_id'])

            if status is not None and status['author'] == interacted_user_name:
                action_date = share['status_shared']
                multiplier = time_decay_parameter(action_date)
                shares_rank += share_weight * multiplier


    sum_affinity = comment_rank + reacts_rank + shares_rank

    return sum_affinity

def status_popularity_rank(comment_quantity, share_quantity, like_quantity, love_quantity, wow_quantity, haha_quantity, sad_quantity, angry_quantity, special_quantity):
    share_weight = 80
    comment_weight = 50
    rank = comment_weight * comment_quantity + share_weight * share_quantity + reacts_weight["likes"] * like_quantity + reacts_weight["loves"] * love_quantity +  + reacts_weight["wows"] * wow_quantity + reacts_weight["hahas"] * haha_quantity + reacts_weight["sads"] * sad_quantity + reacts_weight["angrys"] * angry_quantity + reacts_weight["special"] * special_quantity
    return rank





def create_graph(friends, comments, reactions, shares, statuses) -> nx.Graph:
    if_friends_weight = 7500

    graph = nx.Graph()

    for user_id in friends:
        graph.add_node(user_id)
        for interacted_user_id in friends[user_id]:
            user_affinity = calculate_affinity(user_id, interacted_user_id, comments, reactions, shares, statuses)

            if interacted_user_id in friends[user_id]:
                user_affinity += if_friends_weight

            if user_affinity > 0:
                if not graph.has_edge(user_id, interacted_user_id):
                    graph.add_edge(user_id, interacted_user_id, weight=user_affinity)

    return graph

def insert_data(graph, friends, comments, reactions, shares, statuses) -> nx.Graph:
    if_friends_weight = 7500
    user_list = friends.keys()

    for user_id in user_list:
        for interacted_user_id in user_list:
            if user_id != interacted_user_id:

                if user_id not in graph:
                    graph.add_node(user_id)
                if interacted_user_id not in graph:
                    graph.add_node(interacted_user_id)

                user_affinity = calculate_affinity(user_id, interacted_user_id, comments, reactions, shares, statuses)

                if interacted_user_id in friends[user_id]:
                    user_affinity += if_friends_weight

                if user_affinity > 0:
                    if not graph.has_edge(user_id, interacted_user_id):
                        graph.add_edge(user_id, interacted_user_id, weight=user_affinity)
                    else:
                        graph[user_id][interacted_user_id]['weight'] += user_affinity

    with open("graph.pickle", "wb") as file:
        pickle.dump(graph, file)

    nx.write_graphml(graph, "graph.graphml")
    return graph

    
def get_affinity_graph(friends, comments, reactions, shares, statuses):
    try:
        with open("graph.pickle", "rb") as file:
            graph = pickle.load(file)
        print("Serializovani graf je ucitan.")
        return graph
    except FileNotFoundError:
        print("Graf nije pronadjen! Pravljenje novog u toku...")
        start = datetime.now()
        graph = create_graph(friends, comments, reactions, shares, statuses)
        print(f"Graf je uspesno napravljen posle {datetime.now() - start}")
        with open("graph.pickle", "wb") as file:
            pickle.dump(graph, file)
        return graph


