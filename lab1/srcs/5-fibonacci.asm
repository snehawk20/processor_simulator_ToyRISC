	.data
n:
	1
	.text
main:
	load %x0, $n, %x3           @load n into x3
	add %x0, %x0, %x4           @x4-offeset given for store instruction
	addi %x0, 0, %x5            @x5 = f(n-2), initiated by 0 = f(1)
	addi %x0, 1, %x6			@x6 = f(n-1), initiated by 1 = f(2)
	beq %x3, %x6, endl
	store %x5, 65535, %x4		@storing x5 at memory address 2^16-1
	store %x6, 65534, %x4       @storing x6 at memory address 2^16-2
	addi %x0, 2, %x9			@x9 used for checking base case
	beq %x3, %x9, endl
	subi %x4, 2, %x4			@updating x4 = -2 to give negative offset
	sub %x0, %x3, %x8           @load -n into x8
loop:
	add %x5, %x6, %x7           @x7 = f(n) = f(n-1)+f(n-2)
	store %x7, 65535, %x4       @storing x7 into memory
	subi %x4, 1, %x4			@decrementing the offset
	beq %x4, %x8, endl          @condition to end
	addi %x6, 0, %x5            @updating x5,x6
	addi %x7, 0, %x6
	jmp loop                    @jumping back into the loop
endl:
	end
