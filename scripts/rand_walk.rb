#! /usr/bin/ruby

def random_walk(node, epsilon)
    node.walk_ct += 1
    while true
        alpha = rand
        break if (alpha < epsilon)

        # pick a random neighbor of node
        neighbor = rand * node.degree
        next_node = node.neighbors[neighbor];

        next_node.walk_ct += 1;
        node = next_node;
    end
end

a = [[ 1,1,1,1,1,1,1,1,1,1,1 ],
     [ 0,0,1,0,0,0,0,0,0,0,0 ],
     [ 0,1,0,0,0,0,0,0,0,0,0 ],
     [ 1,1,0,0,0,0,0,0,0,0,0 ],
     [ 0,1,0,1,0,1,0,0,0,0,0 ],
     [ 0,1,0,0,1,0,0,0,0,0,0 ],
     [ 0,1,0,0,1,0,0,0,0,0,0 ],
     [ 0,1,0,0,1,0,0,0,0,0,0 ],
     [ 0,1,0,0,1,0,0,0,0,0,0 ],
     [ 0,0,0,0,1,0,0,0,0,0,0 ],
     [ 0,0,0,0,1,0,0,0,0,0,0 ]]
n = a[0].size
r = 100
epsilon = 0.15

node_class = Struct.new :degree, :neighbors, :walk_ct

nodes = []
for i in 0...n
    nodes[i] = node_class.new(0, [], 0)
end

for i in 0...n
    for j in 0...n
        if a[i][j] == 1
            nodes[i].degree += 1
            nodes[i].neighbors.push(nodes[j])
        end
    end
end

for i in 0...n
    # do R random walks from this node.
    for j in 0...r
        random_walk(nodes[i], epsilon)
    end
end

for i in 0...n
    rank = nodes[i].walk_ct / (n * r / epsilon);
    puts "#{i} #{rank}"
end
