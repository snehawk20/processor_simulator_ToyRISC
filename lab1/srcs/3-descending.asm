	.data
a:
	80
	70
	60
	50
	90
	100
	110
	120
n:
	8
	.text
main:
	add %x0, %x0, %x3            @x3-index position for which sorting is done
	load %x0, $n, %x4            @x4- n
loop:
	load %x3, $a, %x5            @x5- value at x3 index which is considered max at beg,stores max value in program
	addi %x3, 0, %x8             @x8- stores index value of max element found in that iteration
	load %x3, $a, %9             @x9 - temp reg storing initial value at x3 index
	addi %x3, 1, %x6             @x6 - stores x3+1 index value
	beq %x6, %x4, endl           @if x6 = n-> stop, else
	jmp maxf                     @maxf: finding max among elements at the index value between x3->(n-1)
maxf:
	load %x6, $a, %x7            @x7- stores value at x6 index
	bgt %x7, %x5, change		 @if x7 is greater than max elemnt found till now(in x5),
	addi %x6, 1, %x6             @then its value is stored in x5 and its index value in x8,else
	beq %x6, %x4, increment		 @we increment index x6 to compare next element with our max found till now(in x5)
	jmp maxf					 @jumping back to find max						
change:
	addi %x7, 0, %x5
	addi %x6, 0, %x8
	addi %x6, 1, %x6
	beq %x6, %x4, increment      @we increment index x6 to compare next element with our max
	jmp maxf					 @jumping back into loop
increment:
	store %x5, $a, %x3           @storing x5(max) at index x3, and x9 at index x8
	store %x9, $a, %x8
	addi %x3, 1, %x3
	beq %x3, %x4, endl           @condition to end loop.
	jmp loop                     @jumping back into loop
endl:
	end
