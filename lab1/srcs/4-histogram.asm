	.data
count:
	0
	0
	0
	0
	0
	0
	0
	0
	0
	0
	0
marks:
	2
	3
	0
	5
	10
	7
	1
	10
	8
	9
	6
	7
	8
	2
	4
	10
	5
	0
	9
	1
n:
	20
	.text
main:
	add %x0, %x0, %x3	@load 0 into reg3
	load %x0, $n, %x4	@load the number of marks in reg4
loop:
	load %x3, $marks, %x5	@load the ith mark into reg5
	load %x5, $count, %x6	@load the frequency of that mark into reg6
	addi %x6, 1, %x6	@increment that frequency by 1
	store %x6, $count, %x5	@store the frequncy back into memory
	addi %x3, 1, %x3	@increment num in reg3 by 1
	beq %x3, %x4, endl	@once all the inputs have been read, stop
	jmp loop		@otherwise continue
endl:
	end
